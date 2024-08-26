package com.example.webfluxplayground.sec04.mapper;

import com.example.webfluxplayground.sec04.dto.CustomerDto;
import com.example.webfluxplayground.sec04.entity.Customer;

public class EntityDtoMapper {
    public static Customer toEntity(CustomerDto dto){
        return Customer.builder()
                .id(dto.id())
                .name(dto.name())
                .email(dto.email())
                .build();
    }

    public static CustomerDto toDto(Customer entity){
        return CustomerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
