package com.example.webfluxplayground.sec02.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderDetails(
        UUID orderId,
        String customerName,
        String productName,
        Integer amount,
        Instant orderDate
) {
}
