package com.example.webfluxplayground.test.sec07;

import com.example.webfluxplayground.test.sec07.dto.CalculatorResponse;
import com.example.webfluxplayground.test.sec07.dto.Product;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec06QueryParamsTest extends AbstractWebclient{
    //webclient는 non-blocking이다
    private final WebClient client = createWebClient();
    @Test
    public void uriBuilderVariable()  {
        String path = "lec06/calculator";
        String query = "first={first}&second={second}&operation={operation}";
        this.client.post()
                .uri(builder -> builder.path(path).query(query).build(10, 20, "+"))
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
    public void uriBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
        var map = Map.of(
                "first", 10,
                "second", 20,
                "operation", "*"
        );
        this.client.get()
                .uri(builder -> builder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
