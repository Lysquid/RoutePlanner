package fr.blazanome.routeplanner.controller.state;

import java.util.ArrayList;
import java.util.List;

import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import javafx.scene.control.Button;

/**
 * State
 */
public interface State {

    public default void loadMap(Controller controller, String file) {
    }

    public default void undo(Controller controller) {
        controller.getCommandStack().undo();
    }

    public default void redo(Controller controller) {
        controller.getCommandStack().redo();
    }

    public default void selectIntersection(Controller controller, Intersection intersection, Button clicked) {
        clicked.setStyle("-fx-background-color: #ff0000; ");
        System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
        controller.setCurrentState(new IntersectionSelectedState(intersection, clicked));
    }

    public default void addDelivery(Controller controller) {
    };

    public default void compute(Controller controller) {
        TourGenerationAlgorithm algorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1());

        List<Integer> vertices = new ArrayList<>(controller.getDeliveries());
        IMap map = controller.getMap();
        vertices.add(map.getVertexId(map.getWarehouse()));
        List<Integer> path = algorithm.computeTour(controller.getMap(), vertices);
        List<Segment> segmentPath = new ArrayList<>(path.size() - 1);
        for (int i = 0; i < path.size() - 1; i++) {
            segmentPath.add(map.getSegment(path.get(i), path.get(i+1)));
        }

        controller.setCurrentPath(segmentPath);
    }

    public String toString();
}
