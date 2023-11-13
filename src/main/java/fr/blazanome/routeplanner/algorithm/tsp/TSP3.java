package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TSP3 extends TSP2 {

    private class Pair {
        int vertex;
        double weight;

        Pair(int v, double wt) {
            this.vertex = v;
            this.weight = wt;
        }
    }

    protected double prim(Collection<Integer> edges) {
        Integer s0 = edges.iterator().next();
        PriorityQueue<Pair> pq = new PriorityQueue<>(graph.getVerticesCount(), new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return Double.compare(a.weight, b.weight);
            }
        });

        pq.add(new Pair(s0, 0));
        ArrayList<Boolean> visited = new ArrayList<>(Collections.nCopies(graph.getVerticesCount(), false));
        ArrayList<Double> weights = new ArrayList<>(Collections.nCopies(graph.getVerticesCount(), Double.MAX_VALUE));
        weights.set(s0, 0.0);
        double cost = 0;

        while (!pq.isEmpty()) {
            Pair minCostNode = pq.poll();

            if (visited.get(minCostNode.vertex)) {
                continue;
            }

            visited.set(minCostNode.vertex, true);
            //cost += minCostNode.weight;

            for (Integer j : edges) {
                double costToJ = graph.getCost(minCostNode.vertex, j);

                if (costToJ > 0 && !visited.get(j) && costToJ < weights.get(j)) {
                    pq.add(new Pair(j, costToJ));
                    weights.set(j, costToJ);
                }
            }
        }

        double maxCost = 0;
        for (Double d : weights) {
            if (d != Double.MAX_VALUE) {
                cost += d;
                maxCost = Math.max(maxCost, d);
            }
        }
        cost -= maxCost;
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
        return l;
    }

    public static class Factory implements TSP.Factory {

        @Override
        public TSP build() {
            return new TSP3();
        }

    }
}
