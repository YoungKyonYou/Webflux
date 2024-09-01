package com.example.webfluxplayground.test.sec10;

import com.example.webfluxplayground.test.sec10.dto.Product;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class Lec01HttpConnectionPoolingTest extends AbstractWebclient {
    /*
        It is for demo purposes! You might NOT need to adjust all these!
        //보통 이렇게 사용되기 때문에 이걸 넘어갈 때 사용 하면 좋다. 5000req/ sec
        If the response time 100ms => 500 / (100 ms) ==> 5000 req / sec
     */
    private final WebClient client = createWebClient(b -> {
        int poolSize = 10000;
        ConnectionProvider provider = ConnectionProvider.builder("youyk")
                //fifo 보다 lifo를 쓰는 게 더 추천된다.
                .lifo()
                .maxConnections(poolSize)
                //우리는 500 connection를 만들건데 501이나 1000 컨넥션을 만들게 되면 그 request를 queue에 넣게 된다.
                .pendingAcquireMaxCount(poolSize * 5)
                .build();
        //이렇게 설정하면 gzip이나 keepalive 이런 설정들이 없어지기 때문에 여기에 다시 명시해줘야 한다.
        HttpClient httpClient = HttpClient.create(provider)
                //gzip을 사용하겠다.
                .compress(true)
                //HTTP Keep-Alive 기능은 클라이언트와 서버 간의 연결을 유지하고, 여러 요청을 하나의 TCP 연결을 통해 처리할 수 있게 해줍니다.
                // 이 기능을 사용하면 각 요청마다
                //새로운 TCP 연결을 생성하고 종료하는 오버헤드를 줄일 수 있습니다.
                //이 코드는 HTTP 클라이언트가 서버와의 연결을 유지하도록 설정하고 있습니다.
                // 이렇게 하면 서버에 여러 요청을 보낼 때 매번 새로운 연결을 생성하지 않고,
                // 이미 열려 있는 연결을 재사용할 수 있습니다.
                // 이는 네트워크 효율성을 향상시키고, 응답 시간을 줄일 수 있습니다.
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() /*throws InterruptedException*/ {
        //이렇게 만개를 할 수는 없다. 이건 fail할 것이다.
        int max = 10000;
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
