package com.example.webfluxplayground.test.sec10;

import com.example.webfluxplayground.test.sec10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec02Http2Test extends AbstractWebclient {
    /*
        It is for demo purposes! You might NOT need to adjust all these!
        //보통 이렇게 사용되기 때문에 이걸 넘어갈 때 사용 하면 좋다. 5000req/ sec
        If the response time 100ms => 500 / (100 ms) ==> 5000 req / sec
     */
    private final WebClient client = createWebClient(b -> {
        int poolSize = 1;
        ConnectionProvider provider = ConnectionProvider.builder("youyk")
                //fifo 보다 lifo를 쓰는 게 더 추천된다.
                .lifo()
                .maxConnections(poolSize)
                .build();
        //이렇게 설정하면 gzip이나 keepalive 이런 설정들이 없어지기 때문에 여기에 다시 명시해줘야 한다.
        HttpClient httpClient = HttpClient.create(provider)
                //SSL이나 TLS 설정이 있으면 HttpProtocol.H2를 사용하면 된다. 없어서 H2C를 사용함
                        .protocol(HttpProtocol.H2C)
                                .compress(true)
                                        .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() /*throws InterruptedException*/ {
        //이렇게 만개를 할 수는 없다. 이건 fail할 것이다. 
        int max = 3;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> Assertions.assertEquals(max, l.size()))
                .expectComplete()
                .verify();

//        Thread.sleep(Duration.ofMinutes(1).toMillis());
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
