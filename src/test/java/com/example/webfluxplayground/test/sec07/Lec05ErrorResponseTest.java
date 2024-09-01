package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.CalculatorResponse;
import com.example.webfluxplayground.test.sec07.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class Lec05ErrorResponseTest extends AbstractWebclient{
    private final WebClient client = createWebClient();
    @Test
    public void handlingError()  {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10,20)
                .header("operation", "+")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.onErrorReturn(new CalculatorResponse(0,0,null,0.0))
                .doOnError(WebClientResponseException.class, ex -> {
                    log.info("{}", ex.getResponseBodyAs(ProblemDetail.class));
                })
                //remote server가 500 에러를 던질때만 발동된다.
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0,0,null,-1.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0,0,null,-1.0))
                .doOnNext(print())
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void exchange()  {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10,20)
                .header("operation", "+")
                .exchangeToMono(this::decode)
                .doOnNext(print())
                .then() //아무것도 downstream으로 넘기지 않는다.
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<CalculatorResponse> decode(ClientResponse clientResponse){
        //clientResponse.cookies()
        //clientResponse.header()
        log.info("status code: {}", clientResponse.statusCode());
        if(clientResponse.statusCode().isError()){
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(pd -> log.info("{}", pd))
                    .then(Mono.empty());
        }
        return clientResponse.bodyToMono(CalculatorResponse.class);
    }
}
