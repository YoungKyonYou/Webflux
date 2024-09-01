package com.example.webfluxplayground.sec08.controller;

import com.example.webfluxplayground.sec08.dto.ProductDto;
import com.example.webfluxplayground.sec08.dto.UploadResponse;
import com.example.webfluxplayground.sec08.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    /**
     * MediaType.APPLICATION_NDJSON_VALUE는 "application/x-ndjson"을 나타내며,
     * 이는 Newline Delimited JSON (NDJSON) 형식의 데이터를 나타냅니다.
     * NDJSON은 각 JSON 객체가 새로운 줄에 위치하는 JSON 형식입니다.
     * 이 형식은 대량의 JSON 객체를 전송할 때 유용하며, 각 객체를 개별적으로 파싱할 수 있습니다.
     * 각 JSON 객체가 한 줄로 구분되는 형태의 파일 포맷입니다.
     * 이는 데이터 스트리밍에 적합한 형식으로, JSON 객체들이 줄바꿈 문자(\n)로 구분되기 때문에 한 번에 한 줄씩 처리할 수 있습니다.
     */
    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> flux){
        log.info("invoked");
        return this.productService.saveProducts(flux.doOnNext((dto -> log.info("received: {}", dto))))
                .then(this.productService.getProductsCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProducts(){
        return this.productService.allProducts();
    }
}
