package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.model.*;

public class AddRequestCommand implements Command {
    private Courier courier;
    private DeliveryRequest deliveryRequest;

    public AddRequestCommand(Courier courier, DeliveryRequest deliveryRequest) {
        this.courier = courier;
        this.deliveryRequest = deliveryRequest;
    }

    public void apply() {
        courier.addDelivery(this.deliveryRequest);
    }

    public void undo() {
        courier.removeDelivery(this.deliveryRequest);
    }
}
