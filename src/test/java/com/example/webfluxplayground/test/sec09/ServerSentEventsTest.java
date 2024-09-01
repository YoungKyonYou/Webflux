package com.example.webfluxplayground.test.sec09;

import com.example.webfluxplayground.sec09.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest(properties = "spring.profiles.active=sec09")
public class ServerSentEventsTest {
    @Autowired
    private WebTestClient client;

    @Test
    public void serverSentEvents(){
        this.client.get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(3)
                .doOnNext(dto -> log.info("received: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> {
                    Assertions.assertEquals(3, list.size());
                    Assertions.assertTrue(list.stream().allMatch(p -> p.price() <= 80));
                });
    }
}
