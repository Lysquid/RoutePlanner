package fr.blazanome.routeplanner.model;

public class Segment {
    private Intersection destination;
    private double length;
    private String name;

    private Intersection origin;

    public Segment(Intersection destination, double length, String name, Intersection origin) {
        this.destination = destination;
        this.length = length;
        this.name = name;
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public Intersection getDestination() {
        return destination;
    }

    public double getLength() {
        return length;
    }

    public Intersection getOrigin() {
        return origin;
    }
}
