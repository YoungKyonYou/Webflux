package com.example.webfluxplayground.sec03.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Integer id,
        String name,
        String email
) {
}
