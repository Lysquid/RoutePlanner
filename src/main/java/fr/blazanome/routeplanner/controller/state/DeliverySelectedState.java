package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.AddCourierCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

/**
 * DeliverySelectedState
 */
public class DeliverySelectedState implements State {
    @Override
    public void removeDelivery(View view, Session session, Courier courier, Timeframe timeframe, CommandStack commandStack) {
        commandStack.add(new ReverseCommand(new AddCourierCommand(courier, session)));
        view.setDisableAddDelivery(true);
    }
}
