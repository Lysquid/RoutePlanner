package fr.blazanome.routeplanner.graph;

import java.util.ArrayList;
import java.util.List;

import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;

/**
 * Graph
 * Represents a simple graph composed of edges, vertices and a cost function
 */
public interface Graph {

    /**
     * @return The number of vertices in the graph
     */
    int getVerticesCount();

    /**
     * @param vertex
     * @return an iterator of all neighbor of <code>vertex</code>
     */
    Iterable<Neighbor> getNeighbors(int vertex);

    /**
     * @param start Start vertex
     * @param end   End vertex
     * @return the cost of the segment between start and end or a negative number if
     *         <code>start</code> and <code>end</code>
     *         are not neighbor
     */
    double getCost(int start, int end);

    /**
     * Apply the given filter and keep only the edges for which the filter
     * returns true
     * 
     * @param filter
     */
    void filterEdges(EdgeFilter filter);

    /**
     * @param start Start vertex
     * @param end   End vertex
     * @return whether <code>start</code> and <code>end</code> are neighbor
     */
    default boolean isArc(int start, int end) {
        return getCost(start, end) >= 0;
    }
}
