package com.example.clean.entities;

import java.math.BigDecimal;
import java.util.UUID;

public record Item(String id, String name, BigDecimal price) {

    public static Item create(String name, BigDecimal price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        return new Item(UUID.randomUUID().toString(), name, price);
    }
}
