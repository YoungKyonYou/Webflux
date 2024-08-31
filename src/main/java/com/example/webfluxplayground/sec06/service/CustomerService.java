package com.example.webfluxplayground.sec06.service;

import com.example.webfluxplayground.sec06.dto.CustomerDto;
import com.example.webfluxplayground.sec06.mapper.EntityDtoMapper;
import com.example.webfluxplayground.sec06.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers(){
        return this.customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }

    public Flux<CustomerDto> getAllCustomers(Integer page, Integer size) {
        return this.customerRepository.findBy(PageRequest.of(page - 1, size)) // zero-indexed
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id){
        return this.customerRepository.findById(id)
                .map(EntityDtoMapper::toDto);
    }

    /**
     * 이 코드에서 this.customerRepository::save 메소드는 Mono<Customer>를 반환합니다.
     * 따라서 map을 사용하면 결과는 Mono<Mono<CustomerDto>> 형태가 됩니다.
     * 이는 우리가 원하는 결과가 아닙니다. 우리는 Mono<CustomerDto>를 원하기 때문에,
     * flatMap을 사용하여 내부의 Mono를 병합하여 최종적으로 Mono<CustomerDto>를 얻습니다.
     * flatMap을 사용하여 내부의 Mono를 병합하여 최종적으로 Mono<CustomerDto>를 얻습니다.
     */
    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> mono){
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);

    }

    /**
     * flatMap을 사용하면 변환 함수는 Mono<A>에서 Mono<B>를 반환하는 것이 아니라, A를 입력받아 Mono<B>를 반환합니다.
     */
    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> mono){
        Mono<CustomerDto> map = this.customerRepository.findById(id)
                /**
                 * flatMap(entity -> mono)에서는 entity가 findById(id)로부터 반환된 Customer 엔티티를 의미합니다.
                 * 그러나 이 flatMap은 실제로 entity를 사용하지 않고, 그냥 전달된 mono를 반환합니다.
                 * 여기서 flatMap 대신 map를 쓰면 Mono<Mono<CustomerDto>>가 반환됩니다. 그렇기 때문에 flatMap을 사용합니다.
                 * 이 경우를 풀어서 flatMap으로 해서 사용한다
                 */
                .flatMap(entity -> mono)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(e -> e.setId(id)) // this is safe
                //flatMap을 사용하는 이유는, save 메서드가 Mono<Customer>를 반환하기 때문입니다.
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
        System.out.println("map = " + map.map(o -> o.name()));
        return map;
    }

    public Mono<Boolean> deleteCustomerById(Integer id){
        return this.customerRepository.deleteCustomerById(id);
    }

}
