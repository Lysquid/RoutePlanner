package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLMapParser;
import fr.blazanome.routeplanner.view.View;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void loadMap(Controller controller, View view, File file) {
        try {
            IMap map = controller.getMapParser().parse(file);
            Session session = new Session();
            session.addObserver(view);

            Courier courier1 = new Courier();
            courier1.addObserver(view);
            session.addCourier(courier1);

            session.setMap(map);
            controller.setSession(session);
            controller.setCurrentState(new MapLoadedState());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "NoMapState{}";
    }
}
