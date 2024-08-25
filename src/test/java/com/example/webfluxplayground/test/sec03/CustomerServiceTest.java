package com.example.webfluxplayground.test.sec03;

import com.example.webfluxplayground.sec03.dto.CustomerDto;
import com.example.webfluxplayground.sec03.dto.CustomerDto.CustomerDtoBuilder;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@Slf4j
@SpringBootTest(properties = "sec=sec03")
public class CustomerServiceTest {
    @Autowired
    private WebTestClient client;

    @Test
    public void allCustomers(){
        this.client.get()
                //http://localhost:8080를 제공할 필요가 없다 webTestClient가 제공해준다.
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("{}", list))
                .hasSize(10);
    }

    @Test
    public void paginatedCustomers(){
        this.client.get()
                //http://localhost:8080를 제공할 필요가 없다 webTestClient가 제공해준다.
                .uri("/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                //jsonPath를 사용하려면 expectBody()를 써야 한다
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id" ).isEqualTo(5)
                .jsonPath("$[1].id" ).isEqualTo(6);
    }

    @Test
    public void customerById(){
        this.client.get()
                //http://localhost:8080를 제공할 필요가 없다 webTestClient가 제공해준다.
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                //jsonPath를 사용하려면 expectBody()를 써야 한다
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id" ).isEqualTo(1)
                .jsonPath("$.name" ).isEqualTo("sam")
                .jsonPath("$.email" ).isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer(){
        //create
        CustomerDto dto = CustomerDto.builder()
                .email("marshal@gmail.com")
                .name("marshal")
                .build();

        this.client.post()
                .uri("/customers")
                .bodyValue(dto)
                //이 메소드는 ClientResponse 객체를 반환하는데, 이 객체를 통해 HTTP 응답의 상태 코드, 헤더, 바디 등을 검증할 수 있습니다.
                .exchange()
                //jsonPath를 사용하려면 expectBody()를 써야 한다
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id" ).isEqualTo(11)
                .jsonPath("$.name" ).isEqualTo("marshal")
                .jsonPath("$.email" ).isEqualTo("marshal@gmail.com");
        //delete

        this.client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer(){

        CustomerDto dto = CustomerDto.builder()
                .email("noel@gmail.com")
                .name("noel")
                .build();
        this.client.put()
                .uri("/customers/10")
                .bodyValue(dto)
                //이 메소드는 ClientResponse 객체를 반환하는데, 이 객체를 통해 HTTP 응답의 상태 코드, 헤더, 바디 등을 검증할 수 있습니다.
                .exchange()
                //jsonPath를 사용하려면 expectBody()를 써야 한다
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id" ).isEqualTo(10)
                .jsonPath("$.name" ).isEqualTo("noel")
                .jsonPath("$.email" ).isEqualTo("noel@gmail.com");
    }

    @Test
    public void customerNotFound() {
        // get
        this.client.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        // delete
        this.client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();

        // put
        var dto = new CustomerDto(null, "noel", "noel@gmail.com");
        this.client.put()
                .uri("/customers/11")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();
    }




}
