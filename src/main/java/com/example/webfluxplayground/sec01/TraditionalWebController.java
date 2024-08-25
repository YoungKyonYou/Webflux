package com.example.webfluxplayground.sec01;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("traditional")
public class TraditionalWebController {
    private final RestClient restClient = RestClient.builder().
            baseUrl("http://localhost:7070").build();

    //10개 즉 10초 뒤에 결과가 나옴, 중요한건 요청을 보내고 요청을 끊어도 10초 뒤에 결과가 다 옴
    @GetMapping("products")
    public List<Product> getProducts(){
        List<Product> list = this.restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        log.info("received response: {}",list);
        return list;
    }

    @GetMapping("products2")
    public Flux<Product> getProducts2(){
        List<Product> list = this.restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        log.info("received response: {}",list);
        //이렇게 Flux 반환한다고 reactive이 되진 않는다.
        //이유는 모든 것은 reactive pipeline의 일부분이여야 된다.
        //문제는 위 restClient로 external service를 reactive pipeline 밖에서 호출하기 때문이다.
        return Flux.fromIterable(list);
    }

    @GetMapping("products3")
    public List<Product> getProducts3(){
        List<Product> list = this.restClient.get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        log.info("received response: {}",list);
        return list;
    }
}
