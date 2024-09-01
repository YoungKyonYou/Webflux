package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.Product;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@Slf4j
public class Lec09ExchangeFilterTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator())
            .filter(requestLogger()));
    @Test
    public void exchangeFilter1() {
        this.client.post()
                .uri("/lec09/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    //매번 새로운 token이 생성된다.
    @Test
    public void exchangeFilter2(){
        for (int i = 1; i <= 5; i++) {
            this.client.post()
                    .uri("/lec09/product/{id}", 1)
                    .attribute("enable-logging", i%2 == 0)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .then() //아무것도 downstream으로 넘기지 않는다.
                    .as(StepVerifier::create)
                    .expectComplete()
                    .verify();
        }
    }

    private ExchangeFilterFunction tokenGenerator(){
        //ClientRequest
        return ((request, next) -> {
            String token = UUID.randomUUID().toString().replace("-", "");
            log.info("generated token:{}", token);

            ClientRequest modifiedRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        });
    }

    private ExchangeFilterFunction requestLogger(){
        //ClientRequest
        return ((request, next) -> {
            boolean isEnabled = (boolean) request.attributes().getOrDefault("enable-logging", false);
            if(isEnabled){
                log.info("request url - {} : {}", request.method(), request.url());
            }
            log.info("request url - {} : {}", request.method(), request.url());
            return next.exchange(request);
        });
    }
}
