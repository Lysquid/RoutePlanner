package fr.blazanome.routeplanner.algorithm;

import fr.blazanome.routeplanner.algorithm.tsp.TSP;
import fr.blazanome.routeplanner.graph.PathGraph;
import fr.blazanome.routeplanner.model.Delivery;
import fr.blazanome.routeplanner.model.DeliveryRequest;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Route;
import fr.blazanome.routeplanner.model.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * TwoStepComputeTourAlogrithm
 * Uses a <code>CompleteGraphAlgorithm</code> and a <code>TSP</code> to compute
 * the tour
 */
public class TwoStepTourGenerationAlogrithm implements TourGenerationAlgorithm {

    private CompleteGraphAlgorithm completeGraphAlgorithm;
    private TSP.Factory tspAlgorithmFactory;

    public TwoStepTourGenerationAlogrithm(CompleteGraphAlgorithm completeGraphAlgorithm, TSP.Factory tspAlgorithmFactory) {
        this.completeGraphAlgorithm = completeGraphAlgorithm;
        this.tspAlgorithmFactory = tspAlgorithmFactory;
    }

    @Override
    public Route computeTour(IMap map, List<DeliveryRequest> requests, Consumer<Route> onNewRoute) {
        if (requests.isEmpty())
            return null;

        var warehouseId = map.getVertexId(map.getWarehouse());
        var vertexIds = new ArrayList<>(
                requests.stream().map(DeliveryRequest::getIntersection).map(map::getVertexId).toList());

        vertexIds.add(0, warehouseId);

        PathGraph completeGraph = this.completeGraphAlgorithm.computeCompleteGraph(map, vertexIds);

        completeGraph.filterEdges((graph, v1, v2, cost) -> {
            if (v1 == 0 || v2 == 0) {
                // 0 means it's the warehouse
                return true;
            }
            DeliveryRequest r1 = requests.get(v1 - 1);
            DeliveryRequest r2 = requests.get(v2 - 1);
            return r2.getTimeframe().getStart() >= r1.getTimeframe().getStart();
        });

        Consumer<List<Integer>> onTspFind;
        if (onNewRoute != null) {
            onTspFind = (path) -> {
                Route route = this.buildRoute(map, completeGraph, requests, new ArrayList<>(path));
                if (route != null) {
                    onNewRoute.accept(route);
                }
            };
        } else {
            onTspFind = (unused) -> {
            };
        }

        TSP tspAlgorithm = this.tspAlgorithmFactory.build();
        tspAlgorithm.searchSolution(Integer.MAX_VALUE, completeGraph, onTspFind);
        List<Integer> routeInCompleteGraph = tspAlgorithm.getSolution();

        // The TSP didn't find any solution
        if (routeInCompleteGraph == null) {
            return null;
        }

        return this.buildRoute(map, completeGraph, requests, routeInCompleteGraph);
    }

    private Route buildRoute(IMap map, PathGraph completeGraph, List<DeliveryRequest> requests,
                             List<Integer> routeInCompleteGraph) {
        routeInCompleteGraph.add(0);

        List<Segment> path = new ArrayList<>();
        List<Delivery> planning = new ArrayList<>();
        int time = 8 * 60;

        for (int i = 0; i < routeInCompleteGraph.size() - 1; i++) {
            int a = routeInCompleteGraph.get(i);
            int b = routeInCompleteGraph.get(i + 1);
            time += (int) Math.ceil(completeGraph.getCost(a, b) / 1000 / 15.0 * 60.0);

            if (b != 0) {
                DeliveryRequest request = requests.get(b - 1);
                if (request.getTimeframe().getStart() * 60 > time) {
                    time = request.getTimeframe().getStart() * 60;
                }

                if ((request.getTimeframe().getStart() + 1) * 60 < time) {
                    // Invalid path due to timeframes
                    return null;
                }

                planning.add(new Delivery(request, time));
                time += 5;
            }

            List<Integer> subPath = completeGraph.getPath(a, b);
            for (int j = 0; j < subPath.size() - 1; j++) {
                Segment segment = map.getSegment(subPath.get(j), subPath.get(j + 1));
                path.add(segment);
            }
        }

        return new Route(path, planning);
    }

}
