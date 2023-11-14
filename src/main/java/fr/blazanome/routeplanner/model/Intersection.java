package fr.blazanome.routeplanner.model;

/**
 * A road intersection, it has an id, a latitude and a longitude
 */
public class Intersection {
    private final long id;
    private final double latitude;
    private final double longitude;

    public Intersection(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Intersection that = (Intersection) o;
        return Double.compare(latitude, that.latitude) == 0 && Double.compare(longitude, that.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.latitude) ^ Double.hashCode(this.longitude);
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
