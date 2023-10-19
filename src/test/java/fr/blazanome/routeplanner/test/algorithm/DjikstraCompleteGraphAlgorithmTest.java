package fr.blazanome.routeplanner.test.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.blazanome.routeplanner.algorithm.CompleteGraphAlgoritm;
import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.Neighbor;
import fr.blazanome.routeplanner.graph.PathGraph;

/**
 * DjikstraCompleteGraphAlgorithmTest
 */
public class DjikstraCompleteGraphAlgorithmTest {

    @Test
    public void testCompute() {
        var parentGraph = new SimpleGraph(6);

        parentGraph.addEdge(0, 1, 5);

        parentGraph.addEdge(2, 0, 3);

        parentGraph.addEdge(1, 2, 2);
        parentGraph.addEdge(2, 1, 2);

        parentGraph.addEdge(1, 3, 7);
        parentGraph.addEdge(3, 1, 7);

        parentGraph.addEdge(1, 4, 6);
        parentGraph.addEdge(4, 1, 6);

        parentGraph.addEdge(3, 4, 4);
        parentGraph.addEdge(4, 3, 4);

        parentGraph.addEdge(3, 5, 9);
        parentGraph.addEdge(5, 3, 9);

        parentGraph.addEdge(2, 5, 10);
        parentGraph.addEdge(5, 2, 10);

        parentGraph.addEdge(2, 3, 2);
        parentGraph.addEdge(3, 2, 2);

        CompleteGraphAlgoritm algoritm = new DjikstraCompleteGraphAlgorithm();
        PathGraph pathGraph = algoritm.computeCompleteGraph(parentGraph, Arrays.asList(0, 4, 5));

        assertEquals(3, pathGraph.getVerticesCount());

        assertEquals(Arrays.asList(0, 1, 4), pathGraph.getPath(0, 1));
        assertEquals(11, pathGraph.getCost(0, 1));

        assertEquals(Arrays.asList(4, 3, 2, 0), pathGraph.getPath(1, 0));
        assertEquals(9, pathGraph.getCost(1, 0));

        assertEquals(Arrays.asList(4, 3, 5), pathGraph.getPath(1, 2));
        assertEquals(13, pathGraph.getCost(1, 2));

        assertEquals(Arrays.asList(5, 3, 4), pathGraph.getPath(2, 1));
        assertEquals(13, pathGraph.getCost(2, 1));

        assertEquals(Arrays.asList(0, 1, 2, 5), pathGraph.getPath(0, 2));
        assertEquals(17, pathGraph.getCost(0, 2));

        assertEquals(Arrays.asList(5, 2, 0), pathGraph.getPath(2, 0));
        assertEquals(13, pathGraph.getCost(2, 0));
    }

    private class SimpleGraph implements Graph {
        private List<List<Neighbor>> adjacencyList;

        public SimpleGraph(int vertexCount) {
            this.adjacencyList = new ArrayList<>(vertexCount);
            for (int i = 0; i < vertexCount; i++) {
                this.adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int start, int end, double cost) {
            assert start < this.adjacencyList.size();
            assert end < this.adjacencyList.size();

            this.adjacencyList.get(start).add(new Neighbor(end, cost));
        }

        @Override
        public int getVerticesCount() {
            return this.adjacencyList.size();
        }

        @Override
        public Iterable<Neighbor> getNeighbors(int vertex) {
            assert vertex < this.adjacencyList.size();

            return this.adjacencyList.get(vertex);
        }

        @Override
        public double getCost(int start, int end) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getCost'");
        }

    }
}
