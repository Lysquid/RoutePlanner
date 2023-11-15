package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import fr.blazanome.routeplanner.graph.Neighbor;

/**
 * Provides the implementation of the bound of the TSP using Prim
 */
public class TSP3 extends TSP2 {

    private class Pair {
        public int vertex;
        public double weight;

        Pair(int v, double wt) {
            this.vertex = v;
            this.weight = wt;
        }
    }

    /**
     * Prim method to calculate the cost of the MST used by the bound
     * algorithm for solving the TSP in <code>graph</code>.
     * 
     * @param vertices     the set of vertex that must be in the MST
     * @return the total cost of the MST
     */
    protected double prim(Collection<Integer> vertices) {
        Integer s0 = vertices.iterator().next();
        PriorityQueue<Pair> pq = new PriorityQueue<>(graph.getVerticesCount(), new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Double.compare(a.weight, b.weight);
            }
        });

        pq.add(new Pair(s0, 0));
        boolean[] visited = new boolean[graph.getVerticesCount()];
        double[] weights = new double[graph.getVerticesCount()];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = Double.POSITIVE_INFINITY;
        }

        weights[s0] = 0.0;
        double cost = 0;

        while (!pq.isEmpty()) {
            Pair minCostNode = pq.poll();

            if (visited[minCostNode.vertex]) {
                continue;
            }

            visited[minCostNode.vertex] = true;

            for (Neighbor neighbor : graph.getNeighbors(minCostNode.vertex)) {
                if (!visited[neighbor.getVertex()] && weights[neighbor.getVertex()] > neighbor.getCost()) {
                    pq.add(new Pair(neighbor.getVertex(), neighbor.getCost()));
                    weights[neighbor.getVertex()] = neighbor.getCost();
                }
            }
        }

        for (double d : weights) {
            cost += d;
        }

        return cost;
    }

    /**
     * Bound method of the branch and bound algorithm for solving the TSP in
     * <code>graph</code>.
     * 
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @return a lower bound of the cost of paths in <code>graph</code>, starting
     *         from <code>currentVertex<code/>, visits all the unvisited nodes
     *         exactly once
     *         and returns to 0 (initial node of the TSP).
     */
    @Override
    protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
        double l = Double.MAX_VALUE;
        double m = Double.MAX_VALUE;

        for (Integer unvisitedVertex : unvisited) {
            l = Math.min(l, graph.getCost(currentVertex, unvisitedVertex));
            m = Math.min(m, graph.getCost(unvisitedVertex, 0));
        }

        l += prim(unvisited);
        l += m;
        return l;
    }

    public static class Factory implements TSP.Factory {

        @Override
        public TSP build() {
            return new TSP3();
        }

    }
}
