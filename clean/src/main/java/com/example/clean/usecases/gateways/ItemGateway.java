package com.example.clean.usecases.gateways;

import com.example.clean.entities.Item;

import java.util.Optional;

public interface ItemGateway {
    void save(Item item);

    Optional<Item> findById(String id);
}
