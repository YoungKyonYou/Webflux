package com.example.webfluxplayground.sec05.repository;

import com.example.webfluxplayground.sec05.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    @Modifying
    @Query("delete from customer where id =:id")
    Mono<Boolean> deleteCustomerById(@Param("id") Integer id);

    Flux<Customer> findBy(Pageable pageable);

}
