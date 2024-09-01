package com.example.webfluxplayground.sec08.mapper;

import com.example.webfluxplayground.sec08.dto.ProductDto;
import com.example.webfluxplayground.sec08.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto){
        Product product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }

}
