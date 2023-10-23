package fr.blazanome.routeplanner.controller.state;

import java.io.File;

import fr.blazanome.routeplanner.controller.Controller;


/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void loadMap(Controller controller, File file) {
        // TODO: loadMap
    }

    @Override
    public String toString() {
        return "NoMapState{}";
    }
}
