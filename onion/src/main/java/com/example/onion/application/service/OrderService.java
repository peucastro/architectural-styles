package com.example.onion.application.service;

import com.example.onion.application.repository.ItemRepository;
import com.example.onion.application.repository.OrderRepository;
import com.example.onion.application.repository.UserRepository;
import com.example.onion.domain.model.Item;
import com.example.onion.domain.model.Order;
import com.example.onion.domain.service.PricingService;

import java.util.List;

public class OrderService {

    private final UserRepository users;
    private final ItemRepository items;
    private final OrderRepository orders;
    private final PricingService pricing;

    public OrderService(UserRepository users, ItemRepository items, OrderRepository orders, PricingService pricing) {
        this.users = users;
        this.items = items;
        this.orders = orders;
        this.pricing = pricing;
    }

    public Order place(String userId, List<String> itemIds) {
        if (users.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        List<Item> orderItems = itemIds.stream()
                .map(id -> items.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("item not found: " + id)))
                .toList();
        Order order = Order.place(userId, itemIds, pricing.total(orderItems));
        orders.save(order);
        return order;
    }
}
