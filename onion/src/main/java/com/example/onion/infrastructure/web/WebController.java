package com.example.onion.infrastructure.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.example.onion.application.service.ItemService;
import com.example.onion.application.service.OrderService;
import com.example.onion.application.service.UserService;
import com.example.onion.domain.model.Item;
import com.example.onion.domain.model.Order;
import com.example.onion.domain.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Outer-layer web adapter: depends on application services directly, which is allowed since they are inward. */
public class WebController {

    private final UserService userService;
    private final ItemService itemService;
    private final OrderService orderService;

    public WebController(UserService userService, ItemService itemService, OrderService orderService) {
        this.userService = userService;
        this.itemService = itemService;
        this.orderService = orderService;
    }

    public void registerRoutes(HttpServer server) {
        server.createContext("/users", exchange -> handle(exchange, params -> {
            User user = userService.register(params.get("name"), params.get("email"));
            return "{\"id\":\"" + user.id() + "\"}";
        }));
        server.createContext("/items", exchange -> handle(exchange, params -> {
            Item item = itemService.create(params.get("name"), new BigDecimal(params.get("price")));
            return "{\"id\":\"" + item.id() + "\"}";
        }));
        server.createContext("/orders", exchange -> handle(exchange, params -> {
            List<String> itemIds = Arrays.asList(params.getOrDefault("itemIds", "").split(","));
            Order order = orderService.place(params.get("userId"), itemIds);
            return "{\"id\":\"" + order.id() + "\",\"total\":" + order.total() + "}";
        }));
    }

    private void handle(HttpExchange exchange, RequestHandler handler) throws IOException {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "method not allowed");
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            send(exchange, 201, handler.handle(parseForm(body)));
        } catch (IllegalArgumentException e) {
            send(exchange, 400, e.getMessage());
        } catch (RuntimeException e) {
            send(exchange, 500, "internal error");
        }
    }

    private static Map<String, String> parseForm(String body) {
        Map<String, String> params = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                params.put(decode(kv[0]), decode(kv[1]));
            }
        }
        return params;
    }

    private static String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private static void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    @FunctionalInterface
    private interface RequestHandler {
        String handle(Map<String, String> params);
    }
}
