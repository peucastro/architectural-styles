package com.example.onion;

import com.sun.net.httpserver.HttpServer;
import com.example.onion.application.service.ItemService;
import com.example.onion.application.service.OrderService;
import com.example.onion.application.service.UserService;
import com.example.onion.domain.service.PricingService;
import com.example.onion.infrastructure.persistence.InMemoryItemRepository;
import com.example.onion.infrastructure.persistence.InMemoryOrderRepository;
import com.example.onion.infrastructure.persistence.InMemoryUserRepository;
import com.example.onion.infrastructure.web.WebController;

import java.io.IOException;
import java.net.InetSocketAddress;

/** Composition root: wires infrastructure implementations into the inner layers. */
public class Main {

    public static void main(String[] args) throws IOException {
        var users = new InMemoryUserRepository();
        var items = new InMemoryItemRepository();
        var orders = new InMemoryOrderRepository();

        var web = new WebController(
                new UserService(users),
                new ItemService(items),
                new OrderService(users, items, orders, new PricingService()));

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        web.registerRoutes(server);
        server.start();
        System.out.println("Onion app listening on http://localhost:8081");
    }
}
