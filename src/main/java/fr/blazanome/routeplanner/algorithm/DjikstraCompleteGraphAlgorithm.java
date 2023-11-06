package fr.blazanome.routeplanner.algorithm;

import fr.blazanome.routeplanner.graph.AdjacencyListPathGraph;
import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.Neighbor;
import fr.blazanome.routeplanner.graph.PathGraph;

import java.util.*;

/**
 * DjikstraCompleteGraphAlgorithm
 */
public class DjikstraCompleteGraphAlgorithm implements CompleteGraphAlgorithm {

    @Override
    public PathGraph computeCompleteGraph(Graph parentGraph, List<Integer> vertices) {
        Map<Integer, Integer> vertexIdConversion = new HashMap<>(); // From parent graph index to new graph index
        int i = 0;
        for (int vertex : vertices) {
            vertexIdConversion.put(vertex, i);
            i++;
        }

        AdjacencyListPathGraph finalGraph = new AdjacencyListPathGraph(parentGraph, vertices.size());
        for (int finalGraphStartVertex = 0; finalGraphStartVertex < vertices.size(); finalGraphStartVertex++) {
            int parentStartVertex = vertices.get(finalGraphStartVertex);
            var result = djikstra(parentGraph, parentStartVertex, vertices);

            int[] pi = result.pi();
            double[] costs = result.costs();

            for (int finalGraphEndVertex = 0; finalGraphEndVertex < vertices.size(); finalGraphEndVertex++) {
                if (finalGraphStartVertex == finalGraphEndVertex) {
                    continue;
                }

                int parentEndVertex = vertices.get(finalGraphEndVertex);

                List<Integer> path = new ArrayList<>();
                int current = parentEndVertex;
                while (current != -1 && current != parentStartVertex) {
                    path.add(current);
                    current = pi[current];
                }

                if (current == parentStartVertex) {
                    path.add(parentStartVertex);
                    Collections.reverse(path);

                    finalGraph.addEdge(finalGraphStartVertex, finalGraphEndVertex, costs[parentEndVertex], path);
                }
            }
        }

        return finalGraph;
    }

    private DjikstraResult djikstra(Graph graph, int start, Collection<Integer> finalVertices) {
        PriorityQueue<VertexCost> queue = new PriorityQueue<>(Comparator.comparingDouble(v -> v.cost));

        Set<Integer> toVisit = new HashSet<>(finalVertices);
        toVisit.remove(start);

        double[] d = new double[graph.getVerticesCount()];
        int[] pi = new int[graph.getVerticesCount()];

        for (int i = 0; i < graph.getVerticesCount(); i++) {
            d[i] = Double.POSITIVE_INFINITY;
            pi[i] = -1;
        }

        d[start] = 0;
        queue.add(new VertexCost(start, -1, 0));

        while (!queue.isEmpty() && !toVisit.isEmpty()) {
            VertexCost currentVertex = queue.remove();
            if (currentVertex.cost > d[currentVertex.vertex]) {
                continue;
            }

            d[currentVertex.vertex] = currentVertex.cost;
            pi[currentVertex.vertex] = currentVertex.precedent;

            for (Neighbor neighbor : graph.getNeighbors(currentVertex.vertex)) {
                double cost = d[currentVertex.vertex] + neighbor.getCost();
                if (cost < d[neighbor.getVertex()]) {
                    queue.add(new VertexCost(neighbor.getVertex(), currentVertex.vertex, cost));
                }
            }
        }

        return new DjikstraResult(pi, d);
    }

    private record DjikstraResult(int[] pi, double[] costs) {
    }

    private record VertexCost(int vertex, int precedent, double cost) {
    }
}
