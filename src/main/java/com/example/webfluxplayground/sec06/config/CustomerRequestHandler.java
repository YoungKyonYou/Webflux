package com.example.webfluxplayground.sec06.config;

import com.example.webfluxplayground.sec06.exceptions.ApplicationExceptions;
import com.example.webfluxplayground.sec06.dto.CustomerDto;
import com.example.webfluxplayground.sec06.service.CustomerService;
import com.example.webfluxplayground.sec06.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerRequestHandler {
    private final CustomerService customerService;

    public Mono<ServerResponse> allCustomers(ServerRequest request) {
        //request.pathVariable()
        //request.headers()
        //request.queryParams()
        return this.customerService.getAllCustomers()
                .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));

    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> paginatedCustomers(ServerRequest request) {
        Integer page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        Integer size = request.queryParam("size").map(Integer::parseInt).orElse(3);
        return this.customerService.getAllCustomers(page,size)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);

    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        Mono<ServerResponse> serverResponseMono = request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validatedReq -> this.customerService.updateCustomer(id, validatedReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);

        return serverResponseMono;
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then(ServerResponse.ok().build()); //return empty body
    }
}
