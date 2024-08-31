package com.example.webfluxplayground.sec05.controller;

import com.example.webfluxplayground.sec05.dto.CustomerDto;
import com.example.webfluxplayground.sec05.exceptions.ApplicationExceptions;
import com.example.webfluxplayground.sec05.filter.Category;
import com.example.webfluxplayground.sec05.service.CustomerService;
import com.example.webfluxplayground.sec05.validator.RequestValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> allCustomers(@RequestAttribute("category")Category category){
        System.out.println(category);
        return this.customerService.getAllCustomers();
    }

/*    @GetMapping("/paginated")
    public Flux<CustomerDto> allCustomers(@RequestParam(defaultValue = "1", value = "page") Integer page, @RequestParam(defaultValue = "3", value = "size") Integer size){
        return this.customerService.getAllCustomers(page, size);
    }*/

    //Mono 반환타입을 하려면 collectList()를 하면 된다.
    @GetMapping("/paginated")
    public Mono<List<CustomerDto>> allCustomers(@RequestParam(defaultValue = "1", value = "page") Integer page, @RequestParam(defaultValue = "3", value = "size") Integer size){
        return this.customerService.getAllCustomers(page, size).collectList();
    }

    @GetMapping("/{id}")
    public Mono<CustomerDto> getCustomer(@PathVariable("id") Integer id){
        return this.customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono){
        //아래는 마음에 안든다
/*        Mono<CustomerDto> validatedMono = mono.transform(RequestValidator.validate());
        return this.customerService.saveCustomer(validatedMono);*/
        /**
         * as 메소드는 주어진 변환 함수를 적용하여 현재의 Mono를 새로운 Mono로 변환하는 역할을 합니다.
         * 이 변환 함수는 Mono나 Flux의 변환을 위한 함수를 인자로 받아,
         * 그 함수를 적용한 새로운 Mono나 Flux를 반환합니다.
         */
        return mono.transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable("id") Integer id, @RequestBody Mono<CustomerDto> mono){
        return mono.transform(RequestValidator.validate())
                .as(validReq -> this.customerService.updateCustomer(id, validReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));

 /*               this.customerService.updateCustomer(id, mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());*/
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable("id") Integer id){
        return this.customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then();
    }

}
