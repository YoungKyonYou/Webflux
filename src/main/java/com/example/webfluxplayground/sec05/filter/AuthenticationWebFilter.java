package com.example.webfluxplayground.sec05.filter;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Order(1)
@Service
public class AuthenticationWebFilter implements WebFilter {
   // private final FilterErrorHandler errorHandler;
    private static final Map<String, Category> TOKEN_CATEGORY_MAP = Map.of(
            "secret123", Category.STANDARD,
            "secret456", Category.PRIME
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("auth-token");
        if(Objects.nonNull(token) && (TOKEN_CATEGORY_MAP.containsKey(token))){
            exchange.getAttributes().put("category", TOKEN_CATEGORY_MAP.get(token));
            return chain.filter(exchange);
        }
        //이 메소드는 주어진 Runnable을 실행하고, 그 결과를 Mono<Void>로 반환합니다.
        // Runnable은 결과를 반환하지 않는 작업을 나타내는 함수형 인터페이스입니다.
        //동기적으로 실행할 작업을 정의하고, 그 작업의 완료를 나타내는 Mono<Void>를 반환하기 위함입니다.
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));

        //filter 단에서 ProblemDetail을 보내는 방법
        //return errorHandler.sendProblemDetail(exchange, HttpStatus.UNAUTHORIZED, "Set the vaild token");
    }
}
