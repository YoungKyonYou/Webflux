package com.example.webfluxplayground.sec09.repository;

import com.example.webfluxplayground.sec09.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
