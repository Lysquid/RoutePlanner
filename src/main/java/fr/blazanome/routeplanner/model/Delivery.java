package fr.blazanome.routeplanner.model;

/**
 * Delivery
 */
public class Delivery {

    private DeliveryRequest request;
    private int time;

    public Delivery(DeliveryRequest request, int time) {
        this.request = request;
        this.time = time;
    }

    public DeliveryRequest getRequest() {
        return request;
    }

    public int getTime() {
        return time;
    }


}
