package com.example.webfluxplayground.sec02.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
@Data
public class CustomerOrder {
    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer amount;
    private Instant orderDate;

}
