package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class Lec01MonoTest extends AbstractWebclient{
    private final WebClient client = createWebClient();

    @Test
    public void simpleGet() throws InterruptedException {
        this.client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                //subscribe를 해야 실제로 동작한다.!!
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2).toMillis());

    }

    @Test
    public void concurrentRequests() throws InterruptedException {
        for (int i = 1; i <=5; i++) {
            this.client.get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    //subscribe를 해야 실제로 동작한다.!!
                    .subscribe();
        }

        Thread.sleep(Duration.ofSeconds(2).toMillis());

    }
}
