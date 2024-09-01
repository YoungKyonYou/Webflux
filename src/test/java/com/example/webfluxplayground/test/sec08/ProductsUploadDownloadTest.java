package com.example.webfluxplayground.test.sec08;

import com.example.webfluxplayground.sec08.dto.ProductDto;
import java.nio.file.Path;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/*
    just for demo
 */
@Slf4j
public class ProductsUploadDownloadTest {
    private final ProductClient productClient = new ProductClient();
    @Test
    public void upload(){
        Flux<ProductDto> flux = Flux.range(1,10000000)
                .map(i -> new ProductDto(null, "product-"+i, i));
//                .delayElements(Duration.ofSeconds(2));

        this.productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void download(){
        this.productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
