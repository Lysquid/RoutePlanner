package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.model.*;

public class AddDeliveryCommand implements Command {
    private Courier courier;
    private Intersection selectedIntersection;
    private Timeframe timeframe;
    private DeliveryRequest deliveryRequest;

    public AddDeliveryCommand(Courier courier, Intersection selectedIntersection, Timeframe timeframe) {
        this.courier = courier;
        this.selectedIntersection = selectedIntersection;
        this.timeframe = timeframe;
        this.deliveryRequest = new DeliveryRequest(this.selectedIntersection, timeframe);
    }

    public void apply() {
        courier.addDelivery(this.deliveryRequest);
    }

    public void undo() {
        courier.removeDelivery(this.deliveryRequest);
    }
}
