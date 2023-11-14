package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.AddRequestCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.*;

/**
 * The state activated when a delivery request is selected
 */
public class RequestSelectedState implements State {
    private final DeliveryRequest deliveryRequest;
    private final Courier courier;

    public RequestSelectedState(DeliveryRequest deliveryRequest, Courier courier) {
        this.deliveryRequest = deliveryRequest;
        this.courier = courier;
    }

    @Override
    public void removeRequest(Controller controller, CommandStack commandStack) {
        commandStack.add(new ReverseCommand(new AddRequestCommand(this.courier, this.deliveryRequest)));
        controller.setCurrentState(new MapLoadedState());
    }

    public DeliveryRequest getDeliveryRequest() {
        return this.deliveryRequest;
    }
}
