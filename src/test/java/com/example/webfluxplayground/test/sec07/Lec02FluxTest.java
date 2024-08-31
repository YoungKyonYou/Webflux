package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec02FluxTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient();
    @Test
    public void streamingResponse()  {
        this.client.get()
                .uri("/lec02/product/stream")
                .retrieve()
                .bodyToFlux(Product.class)
                .take(Duration.ofSeconds(3).toMillis())
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
