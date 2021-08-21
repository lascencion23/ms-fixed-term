package com.java.everis.msfixedterm.controller;


import com.java.everis.msfixedterm.entity.FixedTerm;
import com.java.everis.msfixedterm.entity.TypeCustomer;
import com.java.everis.msfixedterm.service.FixedTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RefreshScope
@RestController
@RequestMapping("/fixedTerm")
public class FixedTermController {

    @Autowired
    FixedTermService fixedTermService ;

    @GetMapping("/list")
    public Flux<FixedTerm> list(){
        return fixedTermService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<FixedTerm> findById(@PathVariable String id){
        return fixedTermService.findById(id);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<FixedTerm>> create(@Valid @RequestBody FixedTerm fixedTerm){
    			
    		return fixedTermService.creditExpiredById(fixedTerm.getCustomer().getId())
			.filter(count -> (count > 0)? false : true)
			.flatMap(lg -> fixedTermService.findCustomerById(fixedTerm.getCustomer().getId()))
			.filter(customer -> customer.getTypeCustomer().getValue().equals(TypeCustomer.EnumTypeCustomer.PERSONAL) & fixedTerm.getBalance() >= 0)
            .flatMap(customer -> fixedTermService.countCustomerAccountBank(customer.getId())
                    .filter(count ->  count <1)
                    .flatMap(count -> {
                        fixedTerm.setCustomer(customer);
                        fixedTerm.setBalance(fixedTerm.getBalance() != null ? fixedTerm.getBalance() : 0.0);
                        fixedTerm.setLimitDeposits(1);
                        fixedTerm.setLimitDraft(1);
                        fixedTerm.setDate(LocalDateTime.now());
                        return fixedTermService.create(fixedTerm)
                                .map(ft -> new ResponseEntity<>(ft, HttpStatus.CREATED));
                    })
        ).defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @PutMapping("/update")
    public Mono<ResponseEntity<FixedTerm>> update(@Valid @RequestBody FixedTerm fixedTerm) {

        return fixedTermService.findById(fixedTerm.getId())
                .flatMap(ftDB -> fixedTermService.findCustomerById(fixedTerm.getCustomer().getId())
                                .filter(customer -> customer.getTypeCustomer().getValue().equals(TypeCustomer.EnumTypeCustomer.PERSONAL) & fixedTerm.getBalance() >= 0)
                                .flatMap(customer -> {
                                    fixedTerm.setCustomer(customer);
                                    fixedTerm.setBalance(fixedTerm.getBalance() != null ? fixedTerm.getBalance() : 0.0);
                                    fixedTerm.setLimitDeposits(1);
                                    fixedTerm.setLimitDraft(1);
                                    fixedTerm.setDate(LocalDateTime.now());
                                    return fixedTermService.create(fixedTerm)
                                            .map(ft -> new ResponseEntity<>(ft, HttpStatus.CREATED));
                                })

                        )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return fixedTermService.delete(id)
                .filter(deleteFixedTerm -> deleteFixedTerm)
                .map(deleteFixedTerm -> new ResponseEntity<>("Customer Deleted", HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/findByAccountNumber/{numberAccount}")
    public Mono<FixedTerm> findByAccountNumber(@PathVariable String numberAccount){
        return fixedTermService.findByAccountNumber(numberAccount);
    }

    @GetMapping("/findAccountByCustomerId/{id}")
    public Flux<FixedTerm> findByCustomerAccounts(@PathVariable String id) {
    	return fixedTermService.customerAccountBank(id);
    }
    
    @PutMapping("/updateTransference")
    public Mono<ResponseEntity<FixedTerm>> updateForTransference(@Valid @RequestBody FixedTerm fixedTerm) {
        return fixedTermService.create(fixedTerm)
                .filter(customer -> fixedTerm.getBalance() >= 0)
                .map(ft -> new ResponseEntity<>(ft, HttpStatus.CREATED));
    }

}

