package com.example.onion.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Order(String id, String userId, List<String> itemIds, BigDecimal total) {

    public static Order place(String userId, List<String> itemIds, BigDecimal total) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one item");
        }
        return new Order(UUID.randomUUID().toString(), userId, List.copyOf(itemIds), total);
    }
}
