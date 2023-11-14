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

    public String getDeliveryTime() {
        int h = this.time / 60;
        int m = this.time % 60;
        return String.format("%02d:%02d", h, m);
    }
    public String getDepartureTime() {
        int h = (this.time + 5)/ 60;
        int m = (this.time + 5) % 60;
        return String.format("%02d:%02d", h, m);
    }

    public double getLongitude() {
        return this.request.getLongitude();
    }

    public double getLatitude() {
        return this.request.getLatitude();
    }

}
