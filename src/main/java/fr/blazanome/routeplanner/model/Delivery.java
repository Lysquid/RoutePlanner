package fr.blazanome.routeplanner.model;

/**
 * A delivery corresponds to a deliveryRequest and has an exact time for the delivery to be done bi the courier
 */
public class Delivery {

    private DeliveryRequest request;
    private int time;

    public Delivery(DeliveryRequest request, int time) {
        this.request = request;
        this.time = time;
    }

    /**
     * @return the delivery request that this delivery satisfies
     */
    public DeliveryRequest getRequest() {
        return request;
    }

    /**
     * @return returns time of the delivery (in minutes)
     *         example : 120 corresponds to 02:00
     */
    public int getTime() {
        return time;
    }

    /**
     * @return the deliveryTime at the format "hh:mm"
     */
    public String getDeliveryTime() {
        int h = this.time / 60;
        int m = this.time % 60;
        return String.format("%02d:%02d", h, m);
    }

    /**
     * @return the departure time (5 minutes after the delivery time) at the format "hh:mm"
     */
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
