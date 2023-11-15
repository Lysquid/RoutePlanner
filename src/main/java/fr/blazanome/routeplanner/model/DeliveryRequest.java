package fr.blazanome.routeplanner.model;

/**
 * A delivery request added by the user, it has an intersection and a timeframe for valid delivery hours
 */
public class DeliveryRequest {
    private final Intersection intersection;
    private final Timeframe timeframe;

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
