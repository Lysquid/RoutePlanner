package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.observer.Observer;

public interface View extends Observer {

    void onStateChange(Controller controller, State state);

    void onTaskCountChange(int taskCount);

}
