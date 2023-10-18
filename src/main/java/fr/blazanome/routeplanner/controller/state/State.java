package fr.blazanome.routeplanner.controller.state;

import java.io.File;

import fr.blazanome.routeplanner.controller.Controller;

/**
 * State
 */
public interface State {

    public default void loadMap(Controller controller, File file) {}

    public default void undo(Controller controller) {
        controller.getCommandStack().undo();
    }
    public default void redo(Controller controller) {
        controller.getCommandStack().redo();
    }
}
