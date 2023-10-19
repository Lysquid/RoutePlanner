package fr.blazanome.routeplanner.graph;

/**
 * Graph
 * Represents a simple graph composed of edges, vertices and a cost function
 */
public interface Graph {

    /**
     * @return The number of vertices in the graph
     */
    public int getVerticesCount();

    /**
     * @param vertex
     * @return an iterator of all neighbor of <code>vertex</code>
     */
    public Iterable<Neighbor> getNeighbors(int vertex);

    /**
     * @param start Start vertex
     * @param end   End vertex
     * @return the cost of the segment between start and end or a negative number if
     *         <code>start</code> and <code>end</code>
     *         are not neighbor
     */
    public double getCost(int start, int end);

    /**
     * @param start Start vertex
     * @param end   End vertex
     * @return wether <code>start</code> and <code>end</code> are neighbor
     */
    public default boolean isArc(int start, int end) {
        return getCost(start, end) >= 0;
    }

}
