package com.example.hexagonal;

import com.sun.net.httpserver.HttpServer;
import com.example.hexagonal.adapter.in.http.HttpAdapter;
import com.example.hexagonal.adapter.out.memory.InMemoryItemRepository;
import com.example.hexagonal.adapter.out.memory.InMemoryOrderRepository;
import com.example.hexagonal.adapter.out.memory.InMemoryUserRepository;
import com.example.hexagonal.application.service.ItemService;
import com.example.hexagonal.application.service.OrderService;
import com.example.hexagonal.application.service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;

/** Composition root: the only place where concrete adapters are chosen and wired together. */
public class Main {

    public static void main(String[] args) throws IOException {
        var users = new InMemoryUserRepository();
        var items = new InMemoryItemRepository();
        var orders = new InMemoryOrderRepository();

        var http = new HttpAdapter(
                new UserService(users),
                new ItemService(items),
                new OrderService(users, items, orders));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        http.registerRoutes(server);
        server.start();
        System.out.println("Hexagonal app listening on http://localhost:8080");
    }
}
