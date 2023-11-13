package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.AddDeliveryCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

public class IntersectionSelectedState implements State {
    private final Intersection selectedIntersection;

    public IntersectionSelectedState(Intersection selectedIntersection) {
        this.selectedIntersection = selectedIntersection;
    }

    @Override
    public void selectIntersection(Controller controller, View view, Intersection intersection) {

        if(this.selectedIntersection.equals(intersection)) {
            controller.setCurrentState(new MapLoadedState());
        } else {
            controller.setCurrentState(new IntersectionSelectedState(intersection));
        }
    }

    @Override
    public void addDelivery(View view, Session session, Courier courier, Timeframe timeframe, CommandStack commandStack) {
        commandStack.add(new AddDeliveryCommand(courier, new DeliveryRequest(selectedIntersection, timeframe)));
    }

    public final Intersection getSelectedIntersection() {
        return this.selectedIntersection;
    }

}
