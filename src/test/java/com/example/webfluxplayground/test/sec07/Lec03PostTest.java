package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec03PostTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient();
    @Test
    public void postBodyValue()  {
        Product product = new Product(null, "iphone", 1000);
        this.client.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void postBody()  {
        Mono<Product> mono = Mono.fromSupplier(() -> new Product(null, "iphone", 1000))
                .delayElement(Duration.ofSeconds(1));
        this.client.post()
                .uri("/lec03/product")
                .body(mono, Product.class)
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
