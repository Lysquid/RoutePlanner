package fr.blazanome.routeplanner.model.map;

import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;

/**
 * MapBuilder
 */
public interface MapBuilder {

    /**
     * Add an intersection to the map geing built
     * @param intersection
     */
    public void addIntersection(Intersection intersection);

    /**
     * Add a segment to the map being built
     * @param segment
     */
    public void addSegment(Segment segment);

    /**
     * Set the warehouse of the current map being built
     * @param intersection
     */
    public void setWarehouse(Intersection intersection);

    /**
     * @return the built map
     */
    public IMap build();
}
