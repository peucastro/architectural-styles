package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.Item;

import java.util.Optional;

public interface ItemRepository {
    void save(Item item);

    Optional<Item> findById(String id);
}
