package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.CreateItemUseCase;
import com.example.hexagonal.application.port.out.ItemRepository;
import com.example.hexagonal.domain.Item;

import java.math.BigDecimal;

public class ItemService implements CreateItemUseCase {

    private final ItemRepository items;

    public ItemService(ItemRepository items) {
        this.items = items;
    }

    @Override
    public Item create(String name, BigDecimal price) {
        Item item = Item.create(name, price);
        items.save(item);
        return item;
    }
}
