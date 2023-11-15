package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.observer.Observer;

public interface View extends Observer {

    /**
     * Called each time the controller changes state
     */
    void onStateChange(Controller controller, State state);

    /**
     * Called each time the number of tasks changes
     */
    void onTaskCountChange(int taskCount);

}
