package com.example.onion.application.service;

import com.example.onion.application.repository.ItemRepository;
import com.example.onion.domain.model.Item;

import java.math.BigDecimal;

public class ItemService {

    private final ItemRepository items;

    public ItemService(ItemRepository items) {
        this.items = items;
    }

    public Item create(String name, BigDecimal price) {
        Item item = Item.create(name, price);
        items.save(item);
        return item;
    }
}
