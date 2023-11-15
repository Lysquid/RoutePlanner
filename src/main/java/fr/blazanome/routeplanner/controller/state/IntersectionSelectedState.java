package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.AddRequestCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

/**
 * State activated when an intersection is selected
 */
public class IntersectionSelectedState implements State {
    private final Intersection selectedIntersection;

    public IntersectionSelectedState(Intersection selectedIntersection) {
        this.selectedIntersection = selectedIntersection;
    }

    /**
     * If the selected intersection is clicked a second time, it deselects it,
     * else it changes the state to the new selected intersection
     */
    @Override
    public void selectIntersection(Controller controller, View view, Courier courier, Timeframe timeframe, Intersection intersection) {
        if(this.selectedIntersection.equals(intersection)) {
            controller.addRequest(courier, timeframe);
        } else {
            controller.setCurrentState(new IntersectionSelectedState(intersection));
        }
    }

    @Override
    public void addRequest(Controller controller, Courier courier, Timeframe timeframe, CommandStack commandStack) {
        DeliveryRequest request = new DeliveryRequest(selectedIntersection, timeframe);
        commandStack.add(new AddRequestCommand(courier, request));
        controller.setCurrentState(new RequestSelectedState(request, courier));
    }

    /**
     * @return  the intersection currently selected
     */
    public final Intersection getSelectedIntersection() {
        return this.selectedIntersection;
    }

}
