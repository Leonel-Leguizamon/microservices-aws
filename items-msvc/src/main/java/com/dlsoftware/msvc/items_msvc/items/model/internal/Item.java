package com.dlsoftware.msvc.items_msvc.items.model.internal;

import com.dlsoftware.msvc.libs.commons.entities.ProductEntity;
import lombok.*;

@Data
public class Item {
    @NonNull
    private ProductEntity product;
    private int quantity;

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
