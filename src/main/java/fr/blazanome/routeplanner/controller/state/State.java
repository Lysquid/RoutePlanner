package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.CourierRouteUpdater;
import fr.blazanome.routeplanner.controller.AddCourierCommand;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.tools.XMLSessionSerializer;
import fr.blazanome.routeplanner.view.View;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * State from the state pattern
 */
public interface State {

    /**
     * Load a map from a file
     */
    default void loadMap(Controller controller, File file) {
        try {
            IMap map = controller.getMapParser().parse(file);
            Session session = new Session();
            controller.setSession(session);

            Courier courier1 = new Courier(1);
            session.addCourier(courier1);

            session.setMap(map);
            controller.getCommandStack().reset();
            controller.setCurrentState(new MapLoadedState());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Undo the last action, using the command pattern
     */
    default void undo(CommandStack commandStack) {
        commandStack.undo();
    }

    /**
     * Redo the last undone action, using the command pattern
     */
    default void redo(CommandStack commandStack) {
        commandStack.redo();
    }

    /**
     * Called when an intersection was clicked on the view
     * By default, changes the state to IntersectionSelectedState
     */
    default void selectIntersection(Controller controller, View view, Courier courier, Timeframe timeframe, Intersection intersection) {
        controller.setCurrentState(new IntersectionSelectedState(intersection));
    }

    /**
     * Called when a request is selected in the view
     * By default, changes the state to DeliverySelectState
     */
    default void selectRequest(Controller controller, DeliveryRequest deliveryRequest, Courier courier) {
        controller.setCurrentState(new RequestSelectedState(deliveryRequest, courier));
    }

    /**
     * Add a delivery request to a courier
     */
    default void addRequest(Controller controller, Courier courier, Timeframe timeframe, CommandStack commandStack) {
    }

    ;

    /**
     * Remove a delivery request from a courier
     */
    default void removeRequest(Controller controller, CommandStack commandStack) {
    }

    ;

    /**
     * Compute a route for a courier
     */
    default void compute(Courier courier, CourierRouteUpdater updater, Session session) {
        updater.updateTour(courier, session.getMap());
    }

    /**
     * Add a new courier
     */
    default void addCourier(Session session, CommandStack commandStack) {
        commandStack.add(new AddCourierCommand(new Courier(session.getCouriers().size() + 1), session));
    }

    /**
     * Remove an existing courier
     */
    default void removeCourier(Session session, Courier courier, CommandStack commandStack) {
        commandStack.add(new ReverseCommand(new AddCourierCommand(courier, session)));
    }

    /**
     * Called when a courier is selected in the view
     * By default, set the state to MapLoadedState
     */
    default void selectCourier(Controller controller, Courier courier) {
        if (!(controller.getCurrentState() instanceof IntersectionSelectedState)) {
            controller.setCurrentState(new MapLoadedState());
        }
    }

    /**
     * Save the current session to a file
     */
    default void saveSession(File file, Session session) {
        XMLSessionSerializer serializer = new XMLSessionSerializer();
        serializer.serialize(session.getCouriers(), file);
    }

    ;

    /**
     * Load a session from a file, replacing the list of couriers in the session
     */
    default void loadSession(Controller controller, File file, Session session, View view) {
        XMLSessionSerializer serializer = new XMLSessionSerializer();
        try {
            session.setCouriers(serializer.parse(file, session.getMap()));
            controller.getCommandStack().reset();
            for (Courier courier : session.getCouriers()) {
                controller.compute(courier);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
