package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.Collection;

/**
 * Provides the implementation of the bound of the TSP
 */
public class TSP2 extends TSP1 {
    /**
     * Bound method of the branch and bound algorithm for solving the TSP in
     * <code>graph</code>.
     *
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @return a lower bound of the cost of paths in <code>graph</code>, starting
     * from <code>currentVertex</code>, visits all the unvisited nodes exactly once
     * and returns to 0 (initial node of the TSP).
     */
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
