package com.example.onion.infrastructure.persistence;

import com.example.onion.application.repository.OrderRepository;
import com.example.onion.domain.model.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }
}
