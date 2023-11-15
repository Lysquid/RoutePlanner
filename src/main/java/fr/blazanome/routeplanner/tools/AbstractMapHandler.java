package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.map.MapBuilder;

import org.xml.sax.helpers.DefaultHandler;

/**
 * An AbstractMapHandler is a DefaultHandler from SAX, but that has the ability to build a map given a MapBuilder
 */
public abstract class AbstractMapHandler extends DefaultHandler {

    /**
     * Builds the map from the file handled
     *
     * @param builder the MapBuilder used to build the map
     */
    public abstract void buildMap(MapBuilder builder);
}
