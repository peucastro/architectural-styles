package com.example.onion.domain.service;

import com.example.onion.domain.model.Item;

import java.math.BigDecimal;
import java.util.List;

/** Domain logic that spans several items, so it does not belong to any single entity. */
public class PricingService {

    public BigDecimal total(List<Item> items) {
        return items.stream()
                .map(Item::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
