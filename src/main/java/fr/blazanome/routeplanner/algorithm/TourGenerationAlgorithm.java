package fr.blazanome.routeplanner.algorithm;

import fr.blazanome.routeplanner.model.DeliveryRequest;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Route;

import java.util.List;
import java.util.function.Consumer;

/**
 * TourGenerationAlgorithm
 */
public interface TourGenerationAlgorithm {
    /**
     * @param map 
     * @param vertices all the vertices the tour needs to pass through
     * @param onNewRoute a function to call each a new route is found.
     *                   if set to null, wait until the last solution 
     *                   is found to construct the Route
     * @return a list of vertices representing the route in the graph 
     * going through each vertices. Two vertices next to each other in
     * the list are guarrented to be neighbors
     */
    Route computeTour(IMap map, List<DeliveryRequest> vertices, Consumer<Route> onNewRoute);
}
