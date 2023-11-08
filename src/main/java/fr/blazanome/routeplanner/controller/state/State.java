package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.AddCourierCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.ReverseCommand;
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

    default void undo(CommandStack commandStack) {
        commandStack.undo();
    }

    default void redo(CommandStack commandStack) {
        commandStack.redo();
    }

    default void selectIntersection(Controller controller, View view, Intersection intersection) {
        System.out.println(intersection.getLatitude() + " ; " + intersection.getLongitude());
        controller.setCurrentState(new IntersectionSelectedState(intersection));
        view.setDisableAddDelivery(false);
    }

    default void addDelivery(View view, Session session, Courier courier, Timeframe timeframe, CommandStack commandStack) {
    };

    default void removeDelivery(View view, Session session, Courier courier, Timeframe timeframe, CommandStack commandStack) {
    };

    default void compute(Courier courier, TourGenerationAlgorithm algorithm, Session session) {
        // TODO: Remove la route
        if (courier.getRequests().isEmpty())
            return;

        IMap map = session.getMap();
        Route route = algorithm.computeTour(map, courier.getRequests());
        courier.setRoute(route);
    }

    String toString();

    default void addCourier(Session session, CommandStack commandStack) {
        commandStack.add(new AddCourierCommand(new Courier(session.getCouriers().size()+1), session));
    }

    default void removeCourier(Session session, Courier courier, CommandStack commandStack) {
        commandStack.add(new ReverseCommand(new AddCourierCommand(courier, session)));
    }
}
