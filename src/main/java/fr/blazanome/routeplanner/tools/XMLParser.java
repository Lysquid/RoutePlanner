package fr.blazanome.routeplanner.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class XMLParser {
    private SAXParserFactory factory;
    private SAXParser saxParser;
    private AbstractMapHandler handler;

    public XMLParser(AbstractMapHandler handler) {
        this.factory = SAXParserFactory.newInstance();
        this.saxParser = null;
        this.handler = handler;
    }

    public void parse(File file) throws ParserConfigurationException, SAXException, IOException {
        this.saxParser = this.factory.newSAXParser();
        this.saxParser.parse(file, this.handler);
    }
}
