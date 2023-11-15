package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import fr.blazanome.routeplanner.graph.Neighbor;

public class TSP3 extends TSP2 {

    private class Pair {
        int vertex;
        double weight;

        Pair(int v, double wt) {
            this.vertex = v;
            this.weight = wt;
        }
    }

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
            // cost += minCostNode.weight;

            for (Neighbor neighbor : graph.getNeighbors(minCostNode.vertex)) {
                if (!visited[neighbor.getVertex()] && weights[neighbor.getVertex()] > neighbor.getCost()) {
                    pq.add(new Pair(neighbor.getVertex(), neighbor.getCost()));
                    weights[neighbor.getVertex()] = neighbor.getCost();
                }
            }
        }

        for (Double d : weights) {
            if (d != Double.MAX_VALUE) {
                cost += d;
            }
        }
        return cost;
    }

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
        System.out.println(l);
        return l;
    }

    public static class Factory implements TSP.Factory {

        @Override
        public TSP build() {
            return new TSP3();
        }

    }
}
