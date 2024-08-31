package com.example.webfluxplayground.sec06.validator;

import com.example.webfluxplayground.sec06.dto.CustomerDto;
import com.example.webfluxplayground.sec06.exceptions.ApplicationExceptions;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import reactor.core.publisher.Mono;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter(hasValidEmail())
                .switchIfEmpty(ApplicationExceptions.missingValidEmail());
    }

    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDto> hasValidEmail(){
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
    }
}
