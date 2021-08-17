package com.java.everis.msfixedterm.repository;

import com.java.everis.msfixedterm.entity.FixedTerm;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FixedTermRepository extends ReactiveMongoRepository<FixedTerm, String> {
    Flux<FixedTerm> findByCustomerId(String id);
    Mono<FixedTerm> findByCardNumber(String id);
}
