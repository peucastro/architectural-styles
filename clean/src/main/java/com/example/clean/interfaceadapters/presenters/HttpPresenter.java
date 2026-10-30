package com.example.clean.interfaceadapters.presenters;

import com.example.clean.usecases.createitem.CreateItemOutputBoundary;
import com.example.clean.usecases.placeorder.PlaceOrderOutputBoundary;
import com.example.clean.usecases.registeruser.RegisterUserOutputBoundary;

/**
 * Presenter: implements every output boundary and turns use case results into an HTTP view model.
 * It keeps the last result so the controller can read it after the interactor returns.
 */
public class HttpPresenter implements RegisterUserOutputBoundary, CreateItemOutputBoundary, PlaceOrderOutputBoundary {

    public record ViewModel(int status, String body) {
    }

    private ViewModel viewModel;

    @Override
    public void present(RegisterUserOutputBoundary.Response response) {
        viewModel = new ViewModel(201, "{\"id\":\"" + response.id() + "\"}");
    }

    @Override
    public void present(CreateItemOutputBoundary.Response response) {
        viewModel = new ViewModel(201, "{\"id\":\"" + response.id() + "\"}");
    }

    @Override
    public void present(PlaceOrderOutputBoundary.Response response) {
        viewModel = new ViewModel(201, "{\"id\":\"" + response.id() + "\",\"total\":" + response.total() + "}");
    }

    public ViewModel viewModel() {
        return viewModel;
    }
}
