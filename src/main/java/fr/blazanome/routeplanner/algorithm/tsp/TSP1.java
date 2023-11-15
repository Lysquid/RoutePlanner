package fr.blazanome.routeplanner.algorithm.tsp;

import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.Neighbor;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.StreamSupport;

/**
 * Provides a bound that returns 0 for the TSP and an iterator method unvisitedNeighbor
 */
public class TSP1 extends TemplateTSP {
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
        return 0;
    }

    /**
     * Iterator method to get the unvisited neighbors of the <code>currentVertex</code>
     * in <code>g</code>
     *
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @param g             the graph were to apply the TSP
     * @return an iterable to the unvisited neighbors
     */
    @Override
    protected Iterable<Integer> unvisitedNeighbors(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return () -> StreamSupport.stream(g.getNeighbors(currentVertex).spliterator(), false)
                .filter(neighbor -> unvisited.contains(neighbor.getVertex()))
                .sorted(Comparator.comparingDouble(Neighbor::getCost))
                .map(Neighbor::getVertex)
                .iterator();
    }

    public static class Factory implements TSP.Factory {

        @Override
        public TSP build() {
            return new TSP1();
        }

    }
}
