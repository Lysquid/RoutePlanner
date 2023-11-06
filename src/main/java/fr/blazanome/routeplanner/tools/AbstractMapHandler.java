package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.map.MapBuilder;

import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractMapHandler extends DefaultHandler {

    public abstract void buildMap(MapBuilder builder);
}
