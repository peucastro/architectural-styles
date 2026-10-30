package com.example.hexagonal.adapter.in.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.example.hexagonal.application.port.in.CreateItemUseCase;
import com.example.hexagonal.application.port.in.PlaceOrderUseCase;
import com.example.hexagonal.application.port.in.RegisterUserUseCase;
import com.example.hexagonal.domain.Item;
import com.example.hexagonal.domain.Order;
import com.example.hexagonal.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Primary (driving) adapter: translates HTTP requests into calls on the inbound ports. */
public class HttpAdapter {

    private final RegisterUserUseCase registerUser;
    private final CreateItemUseCase createItem;
    private final PlaceOrderUseCase placeOrder;

    public HttpAdapter(RegisterUserUseCase registerUser, CreateItemUseCase createItem, PlaceOrderUseCase placeOrder) {
        this.registerUser = registerUser;
        this.createItem = createItem;
        this.placeOrder = placeOrder;
    }

    public void registerRoutes(HttpServer server) {
        server.createContext("/users", exchange -> handle(exchange, params -> {
            User user = registerUser.register(params.get("name"), params.get("email"));
            return "{\"id\":\"" + user.id() + "\"}";
        }));
        server.createContext("/items", exchange -> handle(exchange, params -> {
            Item item = createItem.create(params.get("name"), new BigDecimal(params.get("price")));
            return "{\"id\":\"" + item.id() + "\"}";
        }));
        server.createContext("/orders", exchange -> handle(exchange, params -> {
            List<String> itemIds = Arrays.asList(params.getOrDefault("itemIds", "").split(","));
            Order order = placeOrder.place(params.get("userId"), itemIds);
            return "{\"id\":\"" + order.id() + "\",\"total\":" + order.total() + "}";
        }));
    }

    private void handle(HttpExchange exchange, RequestHandler handler) throws IOException {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "method not allowed");
                return;
            }
            String body = readBody(exchange.getRequestBody());
            send(exchange, 201, handler.handle(parseForm(body)));
        } catch (IllegalArgumentException e) {
            send(exchange, 400, e.getMessage());
        } catch (RuntimeException e) {
            send(exchange, 500, "internal error");
        }
    }

    private static String readBody(InputStream in) throws IOException {
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
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
