package com.example.webfluxplayground.test.sec10;

import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Slf4j
public class AbstractWebclient {
    protected <T> Consumer<T> print(){
        return item -> log.info("received: {}", item);
    }
    protected WebClient createWebClient(Consumer<Builder> consumer){
        Builder builder = WebClient.builder()
                .baseUrl("http://localhost:7070/demo02");
        consumer.accept(builder);
        return builder.build();
    }

    protected WebClient createWebClient(){
        return createWebClient(b->{});
    }
}
