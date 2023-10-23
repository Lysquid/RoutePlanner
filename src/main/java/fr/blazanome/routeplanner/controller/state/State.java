package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Intersection;
import javafx.scene.control.Button;

/**
 * State
 */
public interface State {

    public default void loadMap(Controller controller, String file) {}

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

    public default void addDelivery(Controller controller) {};

    public String toString();
}
