package com.example.webfluxplayground.sec09.config;

import com.example.webfluxplayground.sec09.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {
    @Bean
    public Sinks.Many<ProductDto> sink(){
        /**
         * Sinks.Many는 여러 개의 데이터를 받아들일 수 있는 싱크입니다.
         * 이 싱크는 여러 생산자로부터 데이터를 받아들일 수 있습니다.
         * .replay().limit(1)는 이 싱크가 받아들인 데이터 중 가장 최근의 데이터 1개를 재생산할 수 있도록
         * 설정하는 코드입니다.
         * 이는 새로운 구독자가 싱크에 구독하면 가장 최근에 받아들인 데이터를 즉시 받을 수 있게 합니다.
         */
        return Sinks.many().replay().limit(1);
    }
}
