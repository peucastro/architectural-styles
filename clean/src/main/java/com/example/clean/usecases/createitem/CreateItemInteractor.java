package com.example.clean.usecases.createitem;

import com.example.clean.entities.Item;
import com.example.clean.usecases.gateways.ItemGateway;

public class CreateItemInteractor implements CreateItemInputBoundary {

    private final ItemGateway items;
    private final CreateItemOutputBoundary output;

    public CreateItemInteractor(ItemGateway items, CreateItemOutputBoundary output) {
        this.items = items;
        this.output = output;
    }

    @Override
    public void execute(Request request) {
        Item item = Item.create(request.name(), request.price());
        items.save(item);
        output.present(new CreateItemOutputBoundary.Response(item.id()));
    }
}
