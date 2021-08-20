package com.java.everis.msfixedterm.service.impl;

import com.java.everis.msfixedterm.entity.Customer;
import com.java.everis.msfixedterm.entity.FixedTerm;
import com.java.everis.msfixedterm.repository.FixedTermRepository;
import com.java.everis.msfixedterm.service.FixedTermService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FixedTermServiceImpl implements FixedTermService {

	private final WebClient webClient;
	private final ReactiveCircuitBreaker reactiveCircuitBreaker;
	
	String uri = "http://gateway:8090/api/ms-customer/customer/find/{id}";
	
	public FixedTermServiceImpl(ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
		this.webClient = WebClient.builder().baseUrl(this.uri).build();
		this.reactiveCircuitBreaker = circuitBreakerFactory.create("customer");
	}
	

    @Autowired
    FixedTermRepository fixedTermRepository ;

    // Plan A
    @Override
    public Mono<Customer> findCustomerById(String id) {
		return reactiveCircuitBreaker.run(webClient.get().uri(this.uri,id).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Customer.class),
				throwable -> {
					return this.getDefaultCustomer();
				});
    }
    
    // Plan B
  	public Mono<Customer> getDefaultCustomer() {
  		Mono<Customer> customer = Mono.just(new Customer("0", null, null,null,null,null,null,null));
  		return customer;
  	}
    
    @Override
    public Mono<FixedTerm> create(FixedTerm ctaCorriente) {
        return fixedTermRepository.save(ctaCorriente);
    }

    @Override
    public Flux<FixedTerm> findAll() {
        return fixedTermRepository.findAll();
    }

    @Override
    public Mono<FixedTerm> findById(String id) {
        return fixedTermRepository.findById(id) ;
    }

    @Override
    public Mono<FixedTerm> update(FixedTerm ctaCorriente) {
        return fixedTermRepository.save(ctaCorriente);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return fixedTermRepository.findById(id)
                .flatMap(
                        deletectaCorriente -> fixedTermRepository.delete(deletectaCorriente)
                                .then(Mono.just(Boolean.TRUE))
                )
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Mono<Long> countCustomerAccountBank(String id) {
        return fixedTermRepository.findByCustomerId(id).count();
    }

    @Override
    public Mono<FixedTerm> findByCardNumber(String numberAccount) {
        return fixedTermRepository.findByCardNumber(numberAccount);
    }
    
}
