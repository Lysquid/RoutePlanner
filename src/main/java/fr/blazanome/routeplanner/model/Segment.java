package fr.blazanome.routeplanner.model;

import java.util.Objects;

/**
 * A road between two intersections (origin and destination)
 * also has a name (street name) and a length
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(length, segment.length) == 0 && Objects.equals(destination, segment.destination) && Objects.equals(name, segment.name) && Objects.equals(origin, segment.origin);
    }
}
