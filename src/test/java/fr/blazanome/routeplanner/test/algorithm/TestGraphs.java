package fr.blazanome.routeplanner.test.algorithm;

import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.SimpleAdjacencyListsGraph;

/**
 * TestGraphs
 */
public class TestGraphs {

    public static Graph graph1() {
        SimpleAdjacencyListsGraph graph = new SimpleAdjacencyListsGraph(6);

        graph.addEdge(0, 1, 5);

        graph.addEdge(2, 0, 3);

        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 1, 2);

        graph.addEdge(1, 3, 7);
        graph.addEdge(3, 1, 7);

        graph.addEdge(1, 4, 6);
        graph.addEdge(4, 1, 6);

        graph.addEdge(3, 4, 4);
        graph.addEdge(4, 3, 4);

        graph.addEdge(3, 5, 9);
        graph.addEdge(5, 3, 9);

        graph.addEdge(2, 5, 10);
        graph.addEdge(5, 2, 10);

        graph.addEdge(2, 3, 2);
        graph.addEdge(3, 2, 2);

        return graph;
    }
}
