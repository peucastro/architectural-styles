package com.example.clean.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Order(String id, String userId, List<String> itemIds, BigDecimal total) {

    public static Order place(String userId, List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one item");
        }
        BigDecimal total = items.stream()
                .map(Item::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<String> itemIds = items.stream().map(Item::id).toList();
        return new Order(UUID.randomUUID().toString(), userId, itemIds, total);
    }
}
