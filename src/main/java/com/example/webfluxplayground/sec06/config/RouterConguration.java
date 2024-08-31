package com.example.webfluxplayground.sec06.config;

import com.example.webfluxplayground.sec06.exceptions.CustomerNotFoundException;
import com.example.webfluxplayground.sec06.exceptions.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@RequiredArgsConstructor
@Configuration
public class RouterConguration {
    private final CustomerRequestHandler customerRequestHandler;
    private final ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRouters1(){
        return RouterFunctions.route()
                //첫인자가 false면 다음 RouterFunction으로 넘어간다.
                //첫인자가 true면 현재 RouterFunction으로 넘어간다.
//                .GET(req -> false, this.customerRequestHandler::allCustomers)
                //이렇게 nested Router functions 구성 가능
                .path("customers", this::customerRouters2)
//                .GET("/customers", this.customerRequestHandler::allCustomers)
//                .GET("/customers/paginated", this.customerRequestHandler::paginatedCustomers)
//                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
                .POST("/customers", this.customerRequestHandler::saveCustomer)
                //*/1은 1로 끝나는 모든 것을 의미한다.이렇게 정규표현식을 사용할 수 있다.
                .PUT("/customers/{id}", RequestPredicates.path("*/1?"), this.customerRequestHandler::updateCustomer)
                .DELETE("/customers/{id}", this.customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.exceptionHandler::handleException)
                .onError(InvalidInputException.class, this.exceptionHandler::handleException)
                //filter는 제일 먼저 실행된다
                .filter((request, next) -> {
                    //체인 넘기는 방법
                    return next.handle(request);
                    //체인 넘기지 않는 방법
//                    return ServerResponse.badRequest().build();
                })
                //이런 식으로 multiple web filter 구성 가능
                .filter((request, next) -> {
                    //체인 넘기는 방법
                    return next.handle(request);
                    //체인 넘기지 않는 방법
//                    return ServerResponse.badRequest().build();
                })
                .build();
    }

    //이런 식으로 Multiple Router Functions를 구성 가능
/*    @Bean
    public RouterFunction<ServerResponse> customerRouters2(){
        return RouterFunctions.route()
                .GET("/customers", this.customerRequestHandler::allCustomers)
                .GET("/customers/paginated", this.customerRequestHandler::paginatedCustomers)
                .GET("/customers/{id}", this.customerRequestHandler::getCustomer)
                .build();
    }*/

    private RouterFunction<ServerResponse> customerRouters2(){
        return RouterFunctions.route()
                .GET("/paginated", this.customerRequestHandler::paginatedCustomers)
                .GET("/{id}", this.customerRequestHandler::getCustomer)
                .GET(this.customerRequestHandler::allCustomers)
                .build();
    }
}
