package fr.blazanome.routeplanner.controller.state;
import javafx.scene.control.Button;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Intersection;

public class IntersectionSelectedState implements State {
    private final Intersection selectedIntersection;

    public IntersectionSelectedState(Intersection selectedIntersection) {
        this.selectedIntersection = selectedIntersection;
    }

    public Intersection getSelectedIntersection() {
        return selectedIntersection;
    }

    @Override
    public void selectIntersection(Controller controller, Intersection intersection) {

        if(this.selectedIntersection.equals(intersection)) {
            controller.setCurrentState(new NoMapState()); // TODO : set to state with no intersection selected
        } else {
            System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
            controller.setCurrentState(new IntersectionSelectedState(intersection));
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
