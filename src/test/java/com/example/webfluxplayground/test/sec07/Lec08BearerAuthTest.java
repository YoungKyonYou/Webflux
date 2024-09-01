package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec08BearerAuthTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient(b -> b.defaultHeaders(h -> h.setBearerAuth("JWT TOKEN HERE")));
    @Test
    public void defaultHeader() {
        this.client.post()
                .uri("/lec08/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
