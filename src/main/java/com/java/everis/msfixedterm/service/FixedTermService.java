package com.java.everis.msfixedterm.service;

import com.java.everis.msfixedterm.entity.Customer;
import com.java.everis.msfixedterm.entity.FixedTerm;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedTermService {

    public Mono<FixedTerm> create(FixedTerm fixedTerm);

    public Flux<FixedTerm> findAll();

    public Mono<FixedTerm> findById(String id);

    public Mono<FixedTerm> update(FixedTerm fixedTerm);

    public Mono<Boolean> delete(String id);

    Mono<Long> countCustomerAccountBank(String id);

    Flux<FixedTerm> customerAccountBank(String id);
    
    Mono<Customer> findCustomerById(String id);
    
    public Mono<FixedTerm> findByAccountNumber(String numberAccount);
    
    Mono<Long> creditExpiredById(String id);
}
