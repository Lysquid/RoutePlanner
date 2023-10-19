package fr.blazanome.routeplanner.model;

public class Intersection {

    private double latitude;
    private double longitude;

    public Intersection(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
