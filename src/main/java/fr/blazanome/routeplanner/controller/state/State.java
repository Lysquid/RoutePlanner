package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * State
 */
public interface State {

    default void loadMap(Controller controller, View view, File file) {
    }

    default void undo(Controller controller) {
        controller.getCommandStack().undo();
    }

    default void redo(Controller controller) {
        controller.getCommandStack().redo();
    }

    default void selectIntersection(Controller controller, View view, Intersection intersection) {
        System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
        controller.setCurrentState(new IntersectionSelectedState(intersection));
        view.setDisableAddDelivery(false);
    }

    default void addDelivery(Controller controller, View view, Session session, Courier courier, Timeframe timeframe) {
    };

    default void compute(Session session) {
        for (Courier courier : session.getCouriers()) {
            if (courier.getRequests().isEmpty())
                continue;
            TourGenerationAlgorithm algorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                    new TSP1());

            List<Integer> vertices = new ArrayList<>();
            IMap map = session.getMap();
            for (DeliveryRequest request: courier.getRequests()) {
                vertices.add(map.getVertexId(request.getIntersection()));
            }
            vertices.add(map.getVertexId(map.getWarehouse()));
            List<Integer> path = algorithm.computeTour(map, vertices);
            List<Segment> segmentPath = new ArrayList<>(path.size() - 1);
            for (int i = 0; i < path.size() - 1; i++) {
                segmentPath.add(map.getSegment(path.get(i), path.get(i + 1)));
            }

            courier.setCurrentPath(segmentPath);
        }
        session.updatedPaths();
    }

    String toString();

    default void addCourier(Session session) {
        session.addCourier(new Courier());
    }

    default void removeCourier(Session session, Courier courier) {
        session.removeCourier(courier);
    }
}
