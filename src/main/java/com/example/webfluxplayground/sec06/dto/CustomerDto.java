package com.example.webfluxplayground.sec06.dto;

import lombok.Builder;

@Builder
public record CustomerDto(
        Integer id,
        String name,
        String email
) {
}
