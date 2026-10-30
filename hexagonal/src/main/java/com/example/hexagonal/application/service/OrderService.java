package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.PlaceOrderUseCase;
import com.example.hexagonal.application.port.out.ItemRepository;
import com.example.hexagonal.application.port.out.OrderRepository;
import com.example.hexagonal.application.port.out.UserRepository;
import com.example.hexagonal.domain.Item;
import com.example.hexagonal.domain.Order;

import java.util.List;

public class OrderService implements PlaceOrderUseCase {

    private final UserRepository users;
    private final ItemRepository items;
    private final OrderRepository orders;

    public OrderService(UserRepository users, ItemRepository items, OrderRepository orders) {
        this.users = users;
        this.items = items;
        this.orders = orders;
    }

    @Override
    public Order place(String userId, List<String> itemIds) {
        if (users.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        List<Item> orderItems = itemIds.stream()
                .map(id -> items.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("item not found: " + id)))
                .toList();
        Order order = Order.place(userId, orderItems);
        orders.save(order);
        return order;
    }
}
