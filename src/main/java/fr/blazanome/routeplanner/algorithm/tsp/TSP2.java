package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.Collection;

public class TSP2 extends TSP1 {
	@Override
	protected double bound(Integer currentVertex, Collection<Integer> unvisited) {
        double l = Double.MAX_VALUE;

        for (Integer unvisitedVertex : unvisited) {
            l = Math.min(l, graph.getCost(currentVertex, unvisitedVertex));
        }
        
        double sum = l;

        for (Integer unvisitedDep : unvisited) {
            double lmin = Double.MAX_VALUE;
            for (Integer unvisitedArr : unvisited) {
                if (unvisitedDep != unvisitedArr) {
                    lmin = Math.min(lmin, graph.getCost(unvisitedDep, unvisitedArr));
                }
            }
            lmin = Math.min(lmin, graph.getCost(unvisitedDep, 0));

            sum += lmin;
        }

		return sum;
	}

    public static class Factory implements TSP.Factory {

        @Override
        public TSP build() {
            return new TSP2();
        }

    }
}
