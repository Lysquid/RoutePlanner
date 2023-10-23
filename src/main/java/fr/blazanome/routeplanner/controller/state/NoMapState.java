package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.Controller;


/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void loadMap(Controller controller, String file) {

    }

    @Override
    public String toString() {
        return "NoMapState{}";
    }
}
