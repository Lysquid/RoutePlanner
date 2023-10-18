package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Map;

import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractMapHandler extends DefaultHandler {

    public abstract IMap getMap();
}