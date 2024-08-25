package com.example.webfluxplayground.sec03.controller;

import com.example.webfluxplayground.sec03.dto.CustomerDto;
import com.example.webfluxplayground.sec03.mapper.EntityDtoMapper;
import com.example.webfluxplayground.sec03.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public Flux<CustomerDto> allCustomers(){
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
    public Mono<ResponseEntity<CustomerDto>> getCustomer(@PathVariable("id") Integer id){
        return this.customerService.getCustomerById(id)
                                    .map(ResponseEntity::ok)
                                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono){
        return this.customerService.saveCustomer(mono);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable("id") Integer id, @RequestBody Mono<CustomerDto> mono){
        return this.customerService.updateCustomer(id, mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable("id") Integer id){
        return this.customerService.deleteCustomerById(id)
                .filter(b -> b)
                .map(b -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
