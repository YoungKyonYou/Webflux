package com.example.webfluxplayground.sec09.controller;

import com.example.webfluxplayground.sec09.dto.ProductDto;
import com.example.webfluxplayground.sec09.dto.UploadResponse;
import com.example.webfluxplayground.sec09.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ProductService service;

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> mono){
        return this.service.saveProduct(mono);
    }

    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> productStream(@PathVariable("maxPrice") int maxPrice){
        return this.service.productStream()
                .filter(dto -> dto.price() <= maxPrice);
    }
}
