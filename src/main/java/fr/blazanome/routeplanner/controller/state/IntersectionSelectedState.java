package fr.blazanome.routeplanner.controller.state;
import javafx.scene.control.Button;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Intersection;

public class IntersectionSelectedState implements State {
    private final Intersection selectedIntersection;

    private final Button previousButton;
    public IntersectionSelectedState(Intersection selectedIntersection, Button previousButton) {
        this.selectedIntersection = selectedIntersection;
        this.previousButton=previousButton;
    }

    public Intersection getSelectedIntersection() {
        return selectedIntersection;
    }

    @Override
    public void selectIntersection(Controller controller, Intersection intersection, Button clicked) {
        if (previousButton != null) {
            previousButton.setStyle("-fx-background-color: #ffffff; ");
        }
        if(this.selectedIntersection.equals(intersection)) {
            controller.setCurrentState(new NoMapState()); // TODO : set to state with no intersection selected
        } else {
            clicked.setStyle("-fx-background-color: #ff0000; ");
            System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
            controller.setCurrentState(new IntersectionSelectedState(intersection, clicked));
        }
    }

    @Override
    public void addDelivery(Controller controller) {
        controller.addDelivery(this.selectedIntersection);
    }

    @Override
    public String toString() {
        return "IntersectionSelectedState{" +
                "selectedIntersection=" + selectedIntersection +
                '}';
    }
}
