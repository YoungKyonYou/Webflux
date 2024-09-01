package com.example.webfluxplayground.sec08.repository;

import com.example.webfluxplayground.sec08.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
