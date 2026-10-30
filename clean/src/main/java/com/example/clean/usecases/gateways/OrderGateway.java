package com.example.clean.usecases.gateways;

import com.example.clean.entities.Order;

public interface OrderGateway {
    void save(Order order);
}
