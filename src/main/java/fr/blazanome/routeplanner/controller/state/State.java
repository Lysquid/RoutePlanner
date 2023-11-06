package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * State
 */
public interface State {

    default void loadMap(Controller controller, View view, File file) {
    }

    default void undo(Controller controller) {
        controller.getCommandStack().undo();
    }

    default void redo(Controller controller) {
        controller.getCommandStack().redo();
    }

    default void selectIntersection(Controller controller, View view, Intersection intersection) {
        System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
        controller.setCurrentState(new IntersectionSelectedState(intersection));
        view.setDisableAddDelivery(false);
    }

    default void addDelivery(Controller controller, View view, Session session, Courier courier, Timeframe timeframe) {
    };

    default void compute(TourGenerationAlgorithm algorithm, Session session) {
        for (Courier courier : session.getCouriers()) {
            if (courier.getRequests().isEmpty())
                continue;
            IMap map = session.getMap();
            Route route = algorithm.computeTour(map, courier.getRequests());
            courier.setRoute(route);
        }
        session.updatedPaths();
    }

    String toString();

    default void addCourier(Session session) {
        session.addCourier(new Courier());
    }

    default void removeCourier(Session session, Courier courier) {
        session.removeCourier(courier);
    }
}
