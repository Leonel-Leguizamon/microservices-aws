package com.dlsoftware.msvc.items_msvc.items.clients;

import com.dlsoftware.msvc.items_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "msvc-products",
        path = "/api-products"
//        ,url = "${feign.clients.products.url}"
)
public interface ProductClient {

    /**
     * Lista todos los productos disponibles en API-PRODUCTS
     * @return
     */
    @GetMapping("/all")
    GeneralResponseWrapper<List<ProductEntity>> findAll();

    /**
     * Busca la información de un producto específico de API-PRODUCTS
     * @param id Identificador del producto
     * @return
     */
    @GetMapping("/{id}")
    GeneralResponseWrapper<ProductEntity> findById(
            @PathVariable Long id
    );

    @PostMapping("/")
    GeneralResponseWrapper<ProductEntity> save(
            @RequestBody ProductEntity product
    );

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(
            @PathVariable Long id
    );

    @PutMapping("/{id}")
    GeneralResponseWrapper<ProductEntity> update(
            @PathVariable Long id,
            @RequestBody ProductEntity product
    );

}
