package com.dlsoftware.msvc.items_msvc.items.service;

import com.dlsoftware.msvc.items_msvc.commons.exceptions.BaseException;
import com.dlsoftware.msvc.items_msvc.commons.exceptions.ProductNotFoundException;
import com.dlsoftware.msvc.items_msvc.items.model.internal.Item;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;

import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id) throws ProductNotFoundException;

    ProductEntity save(ProductEntity ProductEntity) throws BaseException;

    ProductEntity update(ProductEntity ProductEntity, Long id) throws BaseException;

    boolean delete(Long id) throws BaseException;
}
