package com.dlsoftware.msvc.products_msvc.products.service.impl;

import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import com.dlsoftware.msvc.products_msvc.products.repository.ProductRepository;
import com.dlsoftware.msvc.products_msvc.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment environment;

    @Override
    @Transactional(readOnly = true)
    public List<ProductEntity> findAll() {
        return productRepository.findAll().stream().map(product -> {
            product.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));
            return product;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductEntity> findById(long id) {
        return productRepository.findById(id)
                                .map(product -> {
                                        product.setPort(
                                                Integer.parseInt(
                                                    Objects.requireNonNull(
                                                            environment.getProperty("local.server.port")
                                                    )
                                                )
                                        );
                                        return product;
                                    });
    }

    @Transactional
    @Override
    public ProductEntity save(ProductEntity product) {
        return this.productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(long id) {
        return this.productRepository.existsById(id);
    }

    @Override
    public ProductEntity update(ProductEntity product) {
        ProductEntity old = this.productRepository.findById(product.getId()).get();
        old.setName(product.getName());
        old.setDescription(product.getDescription());
        old.setPrice(product.getPrice());
        return this.productRepository.save(old);
    }
}
