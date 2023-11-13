package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.observer.Observer;

public interface View extends Observer {

    public void onStateChange(Controller controller, State state);

}
