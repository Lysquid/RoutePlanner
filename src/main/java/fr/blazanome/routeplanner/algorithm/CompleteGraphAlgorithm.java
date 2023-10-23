package fr.blazanome.routeplanner.algorithm;

import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.PathGraph;

/**
 * CompleteGraphAlgoritm
 */
public interface CompleteGraphAlgorithm {

    /**
     * Given a graph and a list of vertices in the graph, construct the complete graph
     * of the list of vertices with the edges being the shortest path between the two
     * vertices in <code>graph</code>
     *
     * @param graph         The base graph from which we want to create the complete graph
     * @param finalVertices All the vertices being included in the final complete graph
     * @return The complete graph composed of the requested vertices and with the
     * edges being the shortest path between them
     */
    public PathGraph computeCompleteGraph(Graph graph, Iterable<Integer> finalVertices);
}
