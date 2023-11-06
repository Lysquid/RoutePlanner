package fr.blazanome.routeplanner.model;

public class DeliveryRequest {
    private final Intersection intersection;
    private final Timeframe timeframe;
    private final String name = "TEST";

    public DeliveryRequest(Intersection intersection, Timeframe timeframe) {
        this.intersection = intersection;
        this.timeframe = timeframe;
    }

    public Intersection getIntersection() {
        return intersection;
    }

    public Timeframe getTimeframe() {
        return timeframe;
    }

    public double getLatitude() {
        return this.intersection.getLatitude();
    }

    public double getLongitude() {
        return this.intersection.getLongitude();
    }

    public String getTimeframeLabel() {
        return this.timeframe.getLabel();
    }
}
