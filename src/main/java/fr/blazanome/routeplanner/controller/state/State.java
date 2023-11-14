package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
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
 * State
 */
public interface State {

    default void loadMap(Controller controller, View view, File file) {
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

    default void undo(CommandStack commandStack) {
        commandStack.undo();
    }

    default void redo(CommandStack commandStack) {
        commandStack.redo();
    }

    default void selectIntersection(Controller controller, View view, Intersection intersection) {
        controller.setCurrentState(new IntersectionSelectedState(intersection));
    }

    default void selectDelivery(Controller controller, DeliveryRequest deliveryRequest, Courier courier) {
        controller.setCurrentState(new DeliverySelectedState(deliveryRequest, courier));
    }

    default void addDelivery(Controller controller, Courier courier, Timeframe timeframe, CommandStack commandStack) {
    };

    default void removeDelivery(Controller controller, CommandStack commandStack) {
    };

    default void compute(Courier courier, TourGenerationAlgorithm algorithm, Session session) {
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

    default void selectCourier(Controller controller, Courier courier) {
        controller.setCurrentState(new MapLoadedState());
    }

    default void saveSession(File file, Session session) {
        XMLSessionSerializer serializer = new XMLSessionSerializer();
        serializer.serialize(session.getCouriers(), file);
    };

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
