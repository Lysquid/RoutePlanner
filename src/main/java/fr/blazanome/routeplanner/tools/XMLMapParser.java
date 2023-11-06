package fr.blazanome.routeplanner.tools;

import org.xml.sax.SAXException;

import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.map.MapBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class XMLMapParser {
    private SAXParserFactory parserFactory;
    private MapBuilderFactory mapBuilderFactory;

    public XMLMapParser(MapBuilderFactory mapBuilderFactory) {
        this.parserFactory = SAXParserFactory.newInstance();
        this.mapBuilderFactory = mapBuilderFactory;
    }

    public IMap parse(File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = this.parserFactory.newSAXParser();
        var mapBuilder = this.mapBuilderFactory.createBuilder();
        var mapHandler = new MapHandler();
        saxParser.parse(file, mapHandler);

        mapHandler.buildMap(mapBuilder);

        return mapBuilder.build();
    }
}
