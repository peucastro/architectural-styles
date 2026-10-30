package com.example.clean.interfaceadapters.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.example.clean.interfaceadapters.presenters.HttpPresenter;
import com.example.clean.usecases.createitem.CreateItemInputBoundary;
import com.example.clean.usecases.placeorder.PlaceOrderInputBoundary;
import com.example.clean.usecases.registeruser.RegisterUserInputBoundary;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Controller: converts HTTP requests into input boundary requests, then reads the view model from the presenter.
 * The HttpServer runs requests on one dispatcher thread, so the presenter's per-request state is safe here.
 */
public class HttpController {

    private final RegisterUserInputBoundary registerUser;
    private final CreateItemInputBoundary createItem;
    private final PlaceOrderInputBoundary placeOrder;
    private final HttpPresenter presenter;

    public HttpController(RegisterUserInputBoundary registerUser, CreateItemInputBoundary createItem,
                          PlaceOrderInputBoundary placeOrder, HttpPresenter presenter) {
        this.registerUser = registerUser;
        this.createItem = createItem;
        this.placeOrder = placeOrder;
        this.presenter = presenter;
    }

    public void registerRoutes(HttpServer server) {
        server.createContext("/users", ex -> handle(ex, p ->
                registerUser.execute(new RegisterUserInputBoundary.Request(p.get("name"), p.get("email")))));
        server.createContext("/items", ex -> handle(ex, p ->
                createItem.execute(new CreateItemInputBoundary.Request(p.get("name"), new BigDecimal(p.get("price"))))));
        server.createContext("/orders", ex -> handle(ex, p ->
                placeOrder.execute(new PlaceOrderInputBoundary.Request(
                        p.get("userId"), Arrays.asList(p.getOrDefault("itemIds", "").split(","))))));
    }

    private void handle(HttpExchange exchange, Consumer<Map<String, String>> useCase) throws IOException {
        try {
            if (!"POST".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "method not allowed");
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            useCase.accept(parseForm(body));
            HttpPresenter.ViewModel vm = presenter.viewModel();
            send(exchange, vm.status(), vm.body());
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
}
