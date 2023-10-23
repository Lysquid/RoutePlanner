package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.IMap;

import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractMapHandler extends DefaultHandler {

    public abstract IMap getMap();
}
