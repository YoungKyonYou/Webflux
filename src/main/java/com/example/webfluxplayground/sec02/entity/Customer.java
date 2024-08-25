package com.example.webfluxplayground.sec02.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("customer") //필수 아님
@ToString
@Data
public class Customer {
    @Id
    private Integer id;
    @Column("name") //필수 아님
    private String name;
    private String email;



}
