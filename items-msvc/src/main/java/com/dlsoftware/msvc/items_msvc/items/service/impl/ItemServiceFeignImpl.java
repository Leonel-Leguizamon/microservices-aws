package com.dlsoftware.msvc.items_msvc.items.service.impl;

import com.dlsoftware.msvc.items_msvc.commons.exceptions.BaseException;
import com.dlsoftware.msvc.items_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.items_msvc.commons.model.GeneralResponseWrapper;
import com.dlsoftware.msvc.items_msvc.items.clients.ProductClient;
import com.dlsoftware.msvc.items_msvc.items.mappers.ItemMapper;
import com.dlsoftware.msvc.items_msvc.items.model.internal.Item;
import com.dlsoftware.msvc.items_msvc.items.service.ItemService;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemServiceFeignImpl implements ItemService {

    private final ProductClient productClient;
    private final ItemMapper itemMapper;

    @Override
    public List<Item> findAll() {
        return productClient.findAll()
                            .getContent()
                            .stream().map(itemMapper::toItem)
                            .toList();
    }

    @Override
    public Item findById(Long id) throws ProductNotFoundException {
        GeneralResponseWrapper<ProductEntity> response;
        try {
            response = productClient.findById(id);
        } catch (FeignException e) {
            throw new ProductNotFoundException(
                    e.getMessage()
            );
        }
        return this.itemMapper.toItem(response.getContent());
    }

    @Override
    public ProductEntity save(ProductEntity ProductEntity) throws BaseException {
        GeneralResponseWrapper<ProductEntity> response;
        try {
            response = productClient.save(ProductEntity);
        } catch (FeignException e) {
            throw new BaseException(
                    e.getMessage()
            );
        }
        return response.getContent();
    }

    @Override
    public ProductEntity update(ProductEntity ProductEntity, Long id) throws BaseException {
        GeneralResponseWrapper<ProductEntity> response;
        try {
            response = productClient.update(id, ProductEntity);
        } catch (FeignException e) {
            throw new BaseException(
                    e.getMessage()
            );
        }
        return response.getContent();
    }

    @Override
    public boolean delete(Long id) throws BaseException {
        ResponseEntity<?> response;
        try {
            response = productClient.delete(id);
        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new BaseException(
                    e.getMessage()
            );
        }
        return response.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
