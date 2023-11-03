package fr.blazanome.routeplanner.controller.state;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLParser;
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
        MapHandler handler = new MapHandler();
        XMLParser parser = new XMLParser(handler);
        try {
            parser.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        Session session = new Session();
        session.addObserver(view);
        session.setMap(handler.getMap());
        controller.setSession(session);
        controller.setCurrentState(new MapLoadedState());
    }

    @Override
    public String toString() {
        return "NoMapState{}";
    }
}
