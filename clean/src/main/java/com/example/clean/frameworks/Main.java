package com.example.clean.frameworks;

import com.sun.net.httpserver.HttpServer;
import com.example.clean.interfaceadapters.controllers.HttpController;
import com.example.clean.interfaceadapters.gateways.InMemoryItemGateway;
import com.example.clean.interfaceadapters.gateways.InMemoryOrderGateway;
import com.example.clean.interfaceadapters.gateways.InMemoryUserGateway;
import com.example.clean.interfaceadapters.presenters.HttpPresenter;
import com.example.clean.usecases.createitem.CreateItemInteractor;
import com.example.clean.usecases.placeorder.PlaceOrderInteractor;
import com.example.clean.usecases.registeruser.RegisterUserInteractor;

import java.io.IOException;
import java.net.InetSocketAddress;

/** Frameworks & drivers ring: the outermost layer, which wires everything together and starts the server. */
public class Main {

    public static void main(String[] args) throws IOException {
        var users = new InMemoryUserGateway();
        var items = new InMemoryItemGateway();
        var orders = new InMemoryOrderGateway();
        var presenter = new HttpPresenter();

        var controller = new HttpController(
                new RegisterUserInteractor(users, presenter),
                new CreateItemInteractor(items, presenter),
                new PlaceOrderInteractor(users, items, orders, presenter),
                presenter);

        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        controller.registerRoutes(server);
        server.start();
        System.out.println("Clean app listening on http://localhost:8082");
    }
}
