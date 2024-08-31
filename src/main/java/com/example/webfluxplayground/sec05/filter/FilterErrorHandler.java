package com.example.webfluxplayground.sec05.filter;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor

public class FilterErrorHandler {
    private final ServerCodecConfigurer codecConfigurer;

    private ServerResponse.Context context;

    @PostConstruct
    private void init(){
        this.context = new ContextImpl(codecConfigurer);
    }

    public Mono<Void> sendProblemDetail(ServerWebExchange serverWebExchange, HttpStatus httpStatus, String message){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(httpStatus, message);
        return ServerResponse.status(httpStatus)
                .bodyValue(problem)
                /**
                 * flatMap 메소드를 사용하는 이유는 writeTo 메소드가 Mono<Void>를 반환하기 때문입니다.
                 * 만약 map 메소드를 사용하면, 결과는 Mono<Mono<Void>> 타입이 될 것입니다.
                 * 이는 원하는 결과가 아니므로, flatMap 메소드를 사용하여 내부의 Mono<Void>를 외부의 Mono로 병합합니다.
                 * 이렇게 함으로써, 결과는 원하는 Mono<Void> 타입이 됩니다.
                 */
                .flatMap(sr -> sr.writeTo(serverWebExchange, context));
    }

    /**
     *
     ContextImpl을 별도로 정의한 이유는 ServerResponse.Context 인터페이스를 구현하여,
     ServerResponse 객체가 HTTP 응답을 작성할 때 필요한 메시지 작성기(HttpMessageWriter)와 뷰 해석기(ViewResolver)를 제공하기 위함입니다.
     * ServerResponse 객체가 응답을 생성하는 데 필요한 설정 정보를 제공합니다.
     *  메시지 작성기(HttpMessageWriter)는 응답 본문을 직렬화하고, 뷰 해석기(ViewResolver)는 템플릿을 해석하여 응답을 생성하는 데 사용됩니다.
     *  ContextImpl을 정의함으로써 ServerCodecConfigurer에서 메시지 작성기를 가져와 직접 제공하는 역할을 하게 됩니다.
     *  ContextImpl은 ServerCodecConfigurer를 사용하여 메시지 작성기 리스트를 생성하는데,
     *  이 리스트는 ServerResponse에서 응답을 생성할 때 사용됩니다.
     *  이와 같은 설정이 필요한 경우, 별도의 구현체를 만들어 이를 제공해야 합니다.
     */
    private record ContextImpl(ServerCodecConfigurer codecConfigurer) implements ServerResponse.Context {

        /**
         * 이 메서드는 ServerResponse가 응답 본문을 작성할 때 사용할 HttpMessageWriter들의 목록을 반환합니다.
         * HttpMessageWriter는 다양한 MIME 타입의 데이터를 HTTP 응답으로 변환하는 데 사용됩니다. 예를 들어, JSON, XML, 텍스트 등을 응답으로 직렬화하는 데 사용됩니다.
         * codecConfigurer.getWriters()를 통해 메시지 작성기들을 가져오며, 이는 ServerCodecConfigurer가
         * 미리 설정한 작성기 리스트를 반환합니다. 이 설정은 Spring WebFlux의 기본 설정을 기반으로 하며, 필요에 따라 커스터마이즈할 수도 있습니다.
         */
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return this.codecConfigurer.getWriters();
        }

        /**
         * ServerResponse가 뷰 기반의 응답(예: 템플릿 엔진을 사용하는 경우)을 생성할 때 사용할 ViewResolver들의 목록을 반환합니다.
         * ViewResolver는 이름으로 지정된 뷰를 찾아서 실제로 렌더링할 뷰 객체를 반환하는 역할을 합니다
         * 예를 들어, Thymeleaf, FreeMarker, Mustache 등의 템플릿 엔진을 사용할 때 ViewResolver가 필요합니다.
         * 현재 ContextImpl에서는 빈 리스트(List.of())를 반환하고 있습니다.
         * 이는 뷰 해석기가 필요하지 않은 상황, 즉, JSON이나 XML 같은 데이터 직렬화 응답만을 처리할 때 주로 사용됩니다.
         */
        @Override
        public List<ViewResolver> viewResolvers() {
            return List.of();
        }
    }

}
