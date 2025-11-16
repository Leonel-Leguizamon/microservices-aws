package com.dlsoftware.msvc.products_msvc.products.api;

import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import com.dlsoftware.msvc.products_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.products_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.products_msvc.products.service.ProductService;
import com.dlsoftware.msvc.products_msvc.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final MessageUtil messageUtil;

    @GetMapping("/all")
    public ResponseEntity<GeneralResponseWrapper<List<ProductEntity>>> findAllProducts(
            @RequestHeader(name = "message-request", required = false) String message
    ) {
        log.info("Find all msvc products");
        log.info("message: {}", message);
        return ResponseEntity.ok(
                GeneralResponseWrapper.<List<ProductEntity>>builder()
                        .content(productService.findAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponseWrapper<ProductEntity>> findById(
            @PathVariable Long id
    ) throws ProductNotFoundException, InterruptedException {
        log.info("Find msvc product by id: {}", id);

        // errores forzados para probar Resilience4J
        if (id.equals(0L)) {
            throw new IllegalStateException("Product not found");
        }
        if (id.equals(1L)) {
            TimeUnit.SECONDS.sleep(3L);
        }
        return ResponseEntity.ok(
                GeneralResponseWrapper.<ProductEntity>builder()
                        .content(
                                productService.findById(id)
                                                .orElseThrow(
                                                    () -> new ProductNotFoundException(
                                                        messageUtil.getMessage("exceptions.product.not-found", new Object[]{id})
                                                ))
                        ).build()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponseWrapper<ProductEntity>> save(
            @RequestBody ProductEntity product
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponseWrapper.<ProductEntity>builder()
                        .content(
                            productService.save(product)
                        )
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        log.info("Delete msvc product by id: {}", id);
        if (this.productService.existsById(id)) {
            this.productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponseWrapper<ProductEntity>> update(
            @PathVariable Long id,
            @RequestBody ProductEntity product
    ) {
        if (this.productService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    GeneralResponseWrapper.<ProductEntity>builder()
                            .content(productService.update(product))
                            .build()
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
