package com.example.webfluxplayground.test.sec08;

import com.example.webfluxplayground.sec08.dto.ProductDto;
import com.example.webfluxplayground.sec08.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080").build();

    //client streaming
    public Mono<UploadResponse> uploadProducts(Flux<ProductDto> flux){
        return this.client.post()
                .uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDto.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    //server streaming
    public Flux<ProductDto> downloadProducts(){
        return this.client.get()
                .uri("/products/download")
                //아래와 같은 content Type으로 받는다
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve().bodyToFlux(ProductDto.class);
    }

}
