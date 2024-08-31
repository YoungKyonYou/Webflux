package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec04HeaderTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));
    @Test
    public void defaultHeader()  {
        this.client.post()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void overrideHeader()  {
        this.client.post()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id","new-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void headersWithMap()  {
        Map<String, String> map = Map.of(
                "caller-id", "new-value",
                "some-key", "some-value"
        );
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
