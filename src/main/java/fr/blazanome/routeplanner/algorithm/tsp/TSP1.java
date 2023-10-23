package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.StreamSupport;

import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.Neighbor;

public class TSP1 extends TemplateTSP {
	@Override
	protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
		return 0;
	}

    @Override
    protected Iterable<Integer> unvisitedNeighbors(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return () -> StreamSupport.stream(g.getNeighbors(currentVertex).spliterator(), false)
            .filter(neighbor -> unvisited.contains(neighbor.getVertex()))
            .sorted(Comparator.comparingDouble(Neighbor::getCost))
            .map(Neighbor::getVertex)
            .iterator();
    }

}
