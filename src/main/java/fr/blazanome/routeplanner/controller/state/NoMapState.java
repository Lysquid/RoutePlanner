package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.CourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.view.View;

import java.io.File;

/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void undo(CommandStack commandStack) {
    }

    @Override
    public void redo(CommandStack commandStack) {
    }

    @Override
    public void selectIntersection(Controller controller, View view, Intersection intersection) {
    }

    @Override
    public void compute(Courier courier, CourierRouteUpdater algorithm, Session session) {
    }

    @Override
    public void addCourier(Session session, CommandStack commandStack) {
    }

    @Override
    public void removeCourier(Session session, Courier courier, CommandStack commandStack) {
    }

    @Override
    public void saveSession(File file, Session session) {
    }

    @Override
    public void loadSession(Controller controller, File file, Session session, View view) {
    }
}
