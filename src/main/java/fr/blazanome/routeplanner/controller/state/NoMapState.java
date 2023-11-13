package fr.blazanome.routeplanner.controller.state;

import java.io.File;

import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.view.View;

/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void addCourier(Session session, CommandStack commandStack) {
        // noop
    }

    @Override
    public void removeCourier(Session session, Courier courier, CommandStack commandStack) {
        // noop
    }

    @Override
    public void selectCourier(Controller controller, Courier courier) {
        // noop
    }

    @Override
    public void loadSession(Controller controller, File file, Session session, View view) {
        // noop
    }

    @Override
    public void saveSession(File file, Session session) {
        // noop
    }
}
