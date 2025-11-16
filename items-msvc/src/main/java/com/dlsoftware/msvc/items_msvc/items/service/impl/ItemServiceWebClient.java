package com.dlsoftware.msvc.items_msvc.items.service.impl;

import com.dlsoftware.msvc.items_msvc.commons.exceptions.BaseException;
import com.dlsoftware.msvc.items_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.items_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.items_msvc.items.mappers.ItemMapper;
import com.dlsoftware.msvc.items_msvc.items.model.internal.Item;
import com.dlsoftware.msvc.items_msvc.items.service.ItemService;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceWebClient implements ItemService {

    private final WebClient webClient;
    private final ItemMapper itemMapper;

    @Override
    public List<Item> findAll() {
        log.info("Calling productClient service for findAll");

        try {
            return this.webClient
                    .get()
                    .uri("/all")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("Error response from productClient: {}", response.statusCode());
                        return Mono.error(new RuntimeException("Error calling product service: " + response.statusCode()));
                    })
                    .bodyToMono(new ParameterizedTypeReference<GeneralResponseWrapper<List<ProductEntity>>>() {})
                    .map(GeneralResponseWrapper::getContent)
                    .flatMapMany(Flux::fromIterable)
                    .map(itemMapper::toItem)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("Exception calling productClient: ", e);
            throw new RuntimeException("Failed to call product service", e);
        }
    }

    @Override
    public Item findById(Long id) throws ProductNotFoundException {
        log.info("Calling productClient service for findById: {}", id);

        try {
            return this.webClient
                    .get()
                    .uri("/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("Error response from productClient for id {}: {}", id, response.statusCode());
                        if (response.statusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.error(new ProductNotFoundException("Product not found with id: " + id));
                        }
                        return Mono.error(new RuntimeException("Error calling product service: " + response.statusCode()));
                    })
                    .bodyToMono(new ParameterizedTypeReference<GeneralResponseWrapper<ProductEntity>>() {})
                    .map(GeneralResponseWrapper::getContent)
                    .map(itemMapper::toItem)
                    .block();
        } catch (Exception e) {
            log.error("Exception calling productClient for id {}: ", id, e);
            throw new ProductNotFoundException(
                    e.getMessage()
            );
        }
    }

    @Override
    public ProductEntity save(ProductEntity ProductEntity) throws BaseException {
        log.info("Calling productClient service for save product: {}", ProductEntity.toString());

        try {
            return this.webClient
                    .post()
                    .uri("/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ProductEntity)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("Error response from productClient for product {}: {}", ProductEntity.toString(), response.statusCode());
                        return Mono.error(new RuntimeException("Error calling product service: " + response.statusCode()));
                    })
                    .bodyToMono(new ParameterizedTypeReference<GeneralResponseWrapper<ProductEntity>>() {})
                    .map(GeneralResponseWrapper::getContent)
                    .block();
        } catch (Exception e) {
            log.error("Exception calling productClient service for save product: {}", ProductEntity.toString(), e);
            throw new BaseException(
                    e.getMessage()
            );
        }
    }

    @Override
    public ProductEntity update(ProductEntity ProductEntity, Long id) {
        log.info("Calling productClient service for update: {}", id);

        try {
            return this.webClient
                    .put()
                    .uri("/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ProductEntity)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("Error response from productClient for id {}: {}", id, response.statusCode());
                        if (response.statusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.error(new ProductNotFoundException("Product not found with id: " + id));
                        }
                        return Mono.error(new RuntimeException("Error calling product service: " + response.statusCode()));
                    })
                    .bodyToMono(new ParameterizedTypeReference<GeneralResponseWrapper<ProductEntity>>() {})
                    .map(GeneralResponseWrapper::getContent)
                    .block();
        } catch (Exception e) {
            log.error("Exception calling productClient for update id {}: ", id, e);
            throw new ProductNotFoundException(
                    e.getMessage()
            );
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("Calling productClient service for delete id: {}", id);

        try {
             this.webClient
                    .delete()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> {
                        log.error("Error response from productClient for id {}: {}", id, response.statusCode());
                        if (response.statusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.error(new ProductNotFoundException("Product not found with id: " + id));
                        }
                        return Mono.error(new RuntimeException("Error calling product service: " + response.statusCode()));
                    })
                    .bodyToMono(new ParameterizedTypeReference<GeneralResponseWrapper<ProductEntity>>() {})
                    .map(GeneralResponseWrapper::getContent)
                    .block();
        } catch (Exception e) {
            log.error("Exception calling productClient for delete id {}: ", id, e);
            throw new ProductNotFoundException(
                    e.getMessage()
            );
        }
        return true;
    }
}
