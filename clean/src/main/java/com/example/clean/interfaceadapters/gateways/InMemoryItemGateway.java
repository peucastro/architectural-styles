package com.example.clean.interfaceadapters.gateways;

import com.example.clean.entities.Item;
import com.example.clean.usecases.gateways.ItemGateway;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryItemGateway implements ItemGateway {

    private final Map<String, Item> items = new ConcurrentHashMap<>();

    @Override
    public void save(Item item) {
        items.put(item.id(), item);
    }

    @Override
    public Optional<Item> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }
}
