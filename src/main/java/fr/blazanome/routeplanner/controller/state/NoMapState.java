package fr.blazanome.routeplanner.controller.state;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLParser;


/**
 * NoMapState
 */
public class NoMapState implements State {

    @Override
    public void loadMap(Controller controller, File file) {
        MapHandler handler = new MapHandler();
        XMLParser parser = new XMLParser(handler);
        try {
            parser.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        controller.setMap(handler.getMap());
        controller.setCurrentState(new MapLoadedState());
    }

    @Override
    public String toString() {
        return "NoMapState{}";
    }
}
