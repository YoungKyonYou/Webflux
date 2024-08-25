package com.example.webfluxplayground.sec01;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("reactive")
public class ReactiveWebController {
     private final WebClient webClient = WebClient.builder()
             .baseUrl("http://localhost:7070")
             .build();

     //매초 결과가 하나씩 나옴, 요청을 보내고 중간에 끊으면 보낸 것에서 끝남
     @GetMapping("products")
     public Flux<Product> getProducts(){
         return this.webClient.get()
                 .uri("/demo01/products")
                 .retrieve()
                 .bodyToFlux(Product.class)
                 .doOnNext(product -> log.info("received product: {}",product));
     }

     @GetMapping(value = "products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
     public Flux<Product> getProductsStream(){
          return this.webClient.get()
                  .uri("/demo01/products")
                  .retrieve()
                  .bodyToFlux(Product.class)
                  .doOnNext(product -> log.info("received product: {}",product));
     }

     @GetMapping("products2")
     public Flux<Product> getProducts2(){
          return this.webClient.get()
                  .uri("/demo01/products/notorious")
                  .retrieve()
                  .bodyToFlux(Product.class)
                  //에러 시그널이 수신된다면 에러를 무시하고 완료 시그널을 수신한다.
                  .onErrorComplete()
                  .doOnNext(product -> log.info("received product: {}",product));
     }
}
