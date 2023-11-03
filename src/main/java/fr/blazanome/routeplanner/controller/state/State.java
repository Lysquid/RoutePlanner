package fr.blazanome.routeplanner.controller.state;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.view.View;

/**
 * State
 */
public interface State {

    public default void loadMap(Controller controller, File file) {
    }

    public default void undo(Controller controller) {
        controller.getCommandStack().undo();
    }

    public default void redo(Controller controller) {
        controller.getCommandStack().redo();
    }

    public default void selectIntersection(Controller controller, View view, Intersection intersection) {
        System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
        controller.setCurrentState(new IntersectionSelectedState(intersection));
        view.setDisableAddDelivery(false);
    }

    public default void addDelivery(Controller controller, View view) {
    };

    public default void compute(Controller controller) {
        TourGenerationAlgorithm algorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1());

        List<Integer> vertices = new ArrayList<>(controller.getDeliveries());
        IMap map = controller.getSession().getMap();
        vertices.add(map.getVertexId(map.getWarehouse()));
        List<Integer> path = algorithm.computeTour(controller.getSession().getMap(), vertices);
        List<Segment> segmentPath = new ArrayList<>(path.size() - 1);
        for (int i = 0; i < path.size() - 1; i++) {
            segmentPath.add(map.getSegment(path.get(i), path.get(i+1)));
        }

        controller.setCurrentPath(segmentPath);
    }

    public String toString();
}
