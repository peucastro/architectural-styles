package com.example.clean.interfaceadapters.gateways;

import com.example.clean.entities.Order;
import com.example.clean.usecases.gateways.OrderGateway;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryOrderGateway implements OrderGateway {

    private final List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }
}
