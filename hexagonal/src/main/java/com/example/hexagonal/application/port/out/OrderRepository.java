package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.Order;

public interface OrderRepository {
    void save(Order order);
}
