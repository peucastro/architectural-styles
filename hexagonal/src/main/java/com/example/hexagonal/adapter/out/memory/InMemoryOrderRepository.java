package com.example.hexagonal.adapter.out.memory;

import com.example.hexagonal.application.port.out.OrderRepository;
import com.example.hexagonal.domain.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }
}
