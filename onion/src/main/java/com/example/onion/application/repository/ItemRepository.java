package com.example.onion.application.repository;

import com.example.onion.domain.model.Item;

import java.util.Optional;

public interface ItemRepository {
    void save(Item item);

    Optional<Item> findById(String id);
}
