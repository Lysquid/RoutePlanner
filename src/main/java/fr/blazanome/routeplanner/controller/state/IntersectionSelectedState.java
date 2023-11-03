package fr.blazanome.routeplanner.controller.state;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.view.View;
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
    public void selectIntersection(Controller controller, View view, Intersection intersection) {

        if(this.selectedIntersection.equals(intersection)) {
            controller.setCurrentState(new NoMapState()); // TODO : set to state with no intersection selected
        } else {
            System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
            controller.setCurrentState(new IntersectionSelectedState(intersection));
        }
        view.setDisableAddDelivery(false);
    }

    @Override
    public void addDelivery(Controller controller, View view, Session session) {
        Courier selectedCourier = session.getCouriers().get(0); // hard coded first courier
        selectedCourier.addDelivery(session.getMap().getVertexId(this.selectedIntersection));
        view.setDisableAddDelivery(true);
    }

    @Override
    public String toString() {
        return "IntersectionSelectedState{" +
                "selectedIntersection=" + selectedIntersection +
                '}';
    }
}
