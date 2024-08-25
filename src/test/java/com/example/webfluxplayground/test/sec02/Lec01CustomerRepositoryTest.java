package com.example.webfluxplayground.test.sec02;

import com.example.webfluxplayground.sec02.entity.Customer;
import com.example.webfluxplayground.sec02.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Slf4j
public class Lec01CustomerRepositoryTest extends AbstractTest{
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll(){
        //findAll이 publisher이다.
        this.customerRepository.findAll()
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .expectNextCount(10) //10명의 고객 //n개의 요소가 발행되기를 기대한다는 의미
                .expectComplete()
                .verify();// ;
    }

    @Test
    public void findById(){
        //findById가 publisher이다. Mono를 반환한다.
        this.customerRepository.findById(2)
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))//n개의 요소가 발행되기를 기대한다는 의미
                .expectComplete()
                .verify();// ;
    }

    @Test
    public void findByName(){
        this.customerRepository.findByName("jake")
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))//n개의 요소가 발행되기를 기대한다는 의미
                .expectComplete()
                .verify();// ;
    }

    @Test
    public void findByEmailEndingWith(){
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))//n개의 요소가 발행되기를 기대한다는 의미
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))//n개의 요소가 발행되기를 기대한다는 의미
                .expectComplete()
                .verify();// ;
    }

    @Test
    public void insertAndDeleteCustomer(){
        // insert
        Customer customer = new Customer();
        customer.setName("marshal");
        customer.setEmail("marshal@gmail.com");
        //save는 mono를 반환한다.
        this.customerRepository.save(customer)
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))//n개의 요소가 발행되기를 기대한다는 의미
                .expectComplete()
                .verify();

        //count
        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        //delete
        this.customerRepository.deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateCustomer(){
        this.customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("noel"))
                //Mono<Mono<Customer>> -> Mono<Customer> 이렇게 하기 위해서 map 대신 flatMap을 사용한다.
                .flatMap(c -> this.customerRepository.save(c))
                .doOnNext(c -> log.info("{}" , c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("noel", c.getName()))
                .expectComplete()
                .verify();// ;

    }
}
