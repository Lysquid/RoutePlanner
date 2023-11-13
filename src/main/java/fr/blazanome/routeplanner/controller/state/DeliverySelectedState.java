package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.AddDeliveryCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

/**
 * DeliverySelectedState
 */
public class DeliverySelectedState implements State {
    private DeliveryRequest deliveryRequest;
    private Courier courier;

    public DeliverySelectedState(DeliveryRequest deliveryRequest, Courier courier) {
        this.deliveryRequest = deliveryRequest;
        this.courier = courier;
    }

    @Override
    public void removeDelivery(Controller controller, CommandStack commandStack) {
        commandStack.add(new ReverseCommand(new AddDeliveryCommand(this.courier, this.deliveryRequest)));
        controller.setCurrentState(new MapLoadedState());
    }
}
