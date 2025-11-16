package com.dlsoftware.msvc.items_msvc.items.mappers;

import com.dlsoftware.msvc.items_msvc.items.model.internal.Item;
import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.Random;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Mapping(target = "product", source = "ProductEntity")
    @Mapping(target = "quantity", expression = "java(getRandomQty())")
    public abstract Item toItem(ProductEntity ProductEntity);

    @Named("getRandomQty")
    protected int getRandomQty(){
        Random r = new Random();
        return r.nextInt(10)+1;
    }
}
