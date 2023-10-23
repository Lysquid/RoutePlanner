package fr.blazanome.routeplanner.algorithm;

import java.util.ArrayList;
import java.util.List;

import fr.blazanome.routeplanner.algorithm.tsp.TSP;
import fr.blazanome.routeplanner.graph.Graph;
import fr.blazanome.routeplanner.graph.PathGraph;

/**
 * TwoStepComputeTourAlogrithm
 * Uses a <code>CompleteGraphAlgorithm</code> and a <code>TSP</code> to compute
 * the tour
 */
public class TwoStepTourGenerationAlogrithm implements TourGenerationAlgorithm {

    private CompleteGraphAlgorithm completeGraphAlgorithm;
    private TSP tspAlgorithm;

    public TwoStepTourGenerationAlogrithm(CompleteGraphAlgorithm completeGraphAlgorithm, TSP tspAlgorithm) {
        this.completeGraphAlgorithm = completeGraphAlgorithm;
        this.tspAlgorithm = tspAlgorithm;
    }

    @Override
    public List<Integer> computeTour(Graph graph, Iterable<Integer> vertices) {
        PathGraph completeGraph = this.completeGraphAlgorithm.computeCompleteGraph(graph, vertices);
        this.tspAlgorithm.searchSolution(Integer.MAX_VALUE, completeGraph);
        List<Integer> routeInCompleteGraph = this.tspAlgorithm.getSolution();
        routeInCompleteGraph.add(0);

        List<Integer> finalRoute = new ArrayList<>();
        int last = -1;
        for (int i = 0; i < routeInCompleteGraph.size() - 1; i++) {
            int a = routeInCompleteGraph.get(i);
            int b = routeInCompleteGraph.get(i + 1);
            for (int vertex : completeGraph.getPath(a, b)) {
                if (vertex != last) {
                    finalRoute.add(vertex);
                    last = vertex;
                }
            }
        }
        return finalRoute;
    }

}
