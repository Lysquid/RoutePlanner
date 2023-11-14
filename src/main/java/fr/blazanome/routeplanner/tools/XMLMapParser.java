package fr.blazanome.routeplanner.tools;

import org.xml.sax.SAXException;

import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.map.MapBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Class to parse the maps XML files
 */
public class XMLMapParser {
    private SAXParserFactory parserFactory;
    private MapBuilderFactory mapBuilderFactory;

    /**
     * @param mapBuilderFactory a MapBuilder factory to build the adequate map implementation
     */
    public XMLMapParser(MapBuilderFactory mapBuilderFactory) {
        this.parserFactory = SAXParserFactory.newInstance();
        this.mapBuilderFactory = mapBuilderFactory;
    }

    /**
     * Parses a file into an IMap (ignores implementation details(
     * @param file the path to the file
     * @return the map parsed from the file, the implementation is determined by the type of MapBuilderFactory given
     * @throws ParserConfigurationException if function was unable to build the parser
     * @throws SAXException if file cannot be parsed correctly
     * @throws IOException if something goes wrong with opening the file
     */
    public IMap parse(File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = this.parserFactory.newSAXParser();
        var mapBuilder = this.mapBuilderFactory.createBuilder();
        var mapHandler = new MapHandler();
        saxParser.parse(file, mapHandler);

        mapHandler.buildMap(mapBuilder);

        return mapBuilder.build();
    }
}
