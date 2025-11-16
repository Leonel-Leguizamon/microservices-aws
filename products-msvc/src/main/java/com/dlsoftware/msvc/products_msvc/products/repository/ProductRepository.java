package com.dlsoftware.msvc.products_msvc.products.repository;

import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
