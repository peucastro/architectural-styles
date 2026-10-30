package com.example.onion.infrastructure.persistence;

import com.example.onion.application.repository.ItemRepository;
import com.example.onion.domain.model.Item;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryItemRepository implements ItemRepository {

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
