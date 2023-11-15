package fr.blazanome.routeplanner.graph;

import java.util.List;

/**
 * PathGraph
 * <p>
 * A kind of graph were the edges represent a path in a bigger graph
 */
public interface PathGraph extends Graph {
    /**
     * @return the parent graph from which the path are from
     */
    Graph getParentGraph();

    /**
     * @return the list of vertices in the parent graph representing the path
     * between start and end
     */
    List<Integer> getPath(int start, int end);
}
