package com.example.webfluxplayground.sec05.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Integer id,
        String name,
        String email
) {
}
