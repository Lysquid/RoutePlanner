package fr.blazanome.routeplanner.graph;

/**
 * EdgeFilter
 */
public interface EdgeFilter {

    /**
     * Called for each edge between two vertex in a graph
     */
    public boolean test(Graph graph, int v1, int v2, double cost);

}
