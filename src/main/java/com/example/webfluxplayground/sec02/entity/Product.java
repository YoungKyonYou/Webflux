package com.example.webfluxplayground.sec02.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
@Data
public class Product {
    @Id
    private Integer id;
    private String description;
    private Integer price;
}
