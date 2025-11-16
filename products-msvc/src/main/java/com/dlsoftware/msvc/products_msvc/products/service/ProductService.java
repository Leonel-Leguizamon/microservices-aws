package com.dlsoftware.msvc.products_msvc.products.service;

import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductEntity> findAll();

    Optional<ProductEntity> findById(long id);

    ProductEntity save(ProductEntity product);

    void deleteById(long id);

    boolean existsById(long id);

    ProductEntity update(ProductEntity product);
}
