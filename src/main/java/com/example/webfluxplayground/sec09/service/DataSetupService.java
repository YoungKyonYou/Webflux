package com.example.webfluxplayground.sec09.service;

import com.example.webfluxplayground.sec09.dto.ProductDto;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CommandLineRunner 인터페이스는 Spring Boot 애플리케이션에서 사용되며,
 * 애플리케이션 시작 시점에 특정 코드를 실행하고 싶을 때 사용합니다. CommandLineRunner
 * 인터페이스를 구현한 클래스는 애플리케이션이 시작된 후 run 메소드를 자동으로 실행합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataSetupService implements CommandLineRunner {
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1, 1000)
                .delayElements(Duration.ofMillis(1000))
                .map(i -> new ProductDto(null, "product-" + i, ThreadLocalRandom.current().nextInt(1, 100)))
                .flatMap(dto ->this.productService.saveProduct(Mono.just(dto)))
                .subscribe();
    }
}
