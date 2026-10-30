package com.example.clean.usecases.placeorder;

import com.example.clean.entities.Item;
import com.example.clean.entities.Order;
import com.example.clean.usecases.gateways.ItemGateway;
import com.example.clean.usecases.gateways.OrderGateway;
import com.example.clean.usecases.gateways.UserGateway;

import java.util.List;

public class PlaceOrderInteractor implements PlaceOrderInputBoundary {

    private final UserGateway users;
    private final ItemGateway items;
    private final OrderGateway orders;
    private final PlaceOrderOutputBoundary output;

    public PlaceOrderInteractor(UserGateway users, ItemGateway items, OrderGateway orders,
                                PlaceOrderOutputBoundary output) {
        this.users = users;
        this.items = items;
        this.orders = orders;
        this.output = output;
    }

    @Override
    public void execute(Request request) {
        if (users.findById(request.userId()).isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        List<Item> orderItems = request.itemIds().stream()
                .map(id -> items.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("item not found: " + id)))
                .toList();
        Order order = Order.place(request.userId(), orderItems);
        orders.save(order);
        output.present(new PlaceOrderOutputBoundary.Response(order.id(), order.total()));
    }
}
