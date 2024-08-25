package com.example.webfluxplayground.sec03.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Customer {
    @Generated
    @Id
    private Integer id;

    private String name;
    private String email;



}
