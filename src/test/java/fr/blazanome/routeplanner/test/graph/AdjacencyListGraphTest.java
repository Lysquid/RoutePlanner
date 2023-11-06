package fr.blazanome.routeplanner.test.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.test.algorithm.TestGraphs;

/**
 * AdjacencyListGraph
 */
public class AdjacencyListGraphTest {
    @Test
    public void testFilter() {
        Graph graph = TestGraphs.graph1();
        graph.filterEdges((g, v1, v2, cost) -> v1 < v2 && cost < 6);

        assertEquals(true, graph.isArc(0, 1));

        assertEquals(false, graph.isArc(2, 0));

        assertEquals(true, graph.isArc(1, 2));
        assertEquals(false, graph.isArc(2, 1));

        assertEquals(false, graph.isArc(1, 3));
        assertEquals(false, graph.isArc(3, 1));

        assertEquals(false, graph.isArc(1, 4));
        assertEquals(false, graph.isArc(4, 1));

        assertEquals(true, graph.isArc(3, 4));
        assertEquals(false, graph.isArc(4, 3));

        assertEquals(false, graph.isArc(3, 5));
        assertEquals(false, graph.isArc(5, 3));

        assertEquals(false, graph.isArc(2, 5));
        assertEquals(false, graph.isArc(5, 2));

        assertEquals(true, graph.isArc(2, 3));
        assertEquals(false, graph.isArc(3, 2));
    }

}
