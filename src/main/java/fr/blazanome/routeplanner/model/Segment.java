package fr.blazanome.routeplanner.model;

public class Segment {
    private long destination;
    private double length;
    private String name;
    private long origin;

    public Segment(long destination, double length, String name, long origin) {
        this.destination = destination;
        this.length = length;
        this.name = name;
        this.origin = origin;
    }

    public String getName() {
        return name;
    }
}
