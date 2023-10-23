package fr.blazanome.routeplanner.algorithm;

import java.util.List;

import fr.blazanome.routeplanner.graph.Graph;

/**
 * TourGenerationAlgorithm
 */
public interface TourGenerationAlgorithm {
    /**
     * @param graph 
     * @param vertices all the vertices the tour needs to pass through
     * @return a list of vertices representing the route in the graph 
     * going through each vertices. Two vertices next to each other in
     * the list are guarrented to be neighbors
     */
    List<Integer> computeTour(Graph graph, Iterable<Integer> vertices);
}
