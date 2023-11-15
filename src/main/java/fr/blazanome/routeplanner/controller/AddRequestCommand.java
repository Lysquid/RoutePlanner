package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.model.*;

/**
 * Command to add a delivery request to a courier
 */
public class AddRequestCommand implements Command {
    private Courier courier;
    private DeliveryRequest deliveryRequest;

    public AddRequestCommand(Courier courier, DeliveryRequest deliveryRequest) {
        this.courier = courier;
        this.deliveryRequest = deliveryRequest;
    }

    /**
     * Apply command : add request to courier
     */
    @Override
    public void apply() {
        courier.addDelivery(this.deliveryRequest);
    }

    /**
     * Undo command : remove request from courier
     */
    @Override
    public void undo() {
        courier.removeDelivery(this.deliveryRequest);
    }
}
