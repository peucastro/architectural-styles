package com.example.onion.application.repository;

import com.example.onion.domain.model.Order;

public interface OrderRepository {
    void save(Order order);
}
