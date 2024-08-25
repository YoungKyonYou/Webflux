package com.example.webfluxplayground.test.sec02;

import com.example.webfluxplayground.sec02.repository.CustomerOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Slf4j
public class Lec03CustomerOrderRepositoryTest extends AbstractTest {
    @Autowired
    private CustomerOrderRepository repository;

    @Test
    public void productOrderedByCustomer(){
        this.repository.getProductsOrderedByCustomer("mike")
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void orderDetailsByProduct(){
        this.repository.getOrderDetailsByProduct("iphone 20")
                .doOnNext(dto -> log.info("{}", dto))
                .as(StepVerifier::create)
                .assertNext(dto -> Assertions.assertEquals(975, dto.amount()))
                .assertNext(dto -> Assertions.assertEquals(950, dto.amount()))
                .expectComplete()
                .verify();
    }
}
