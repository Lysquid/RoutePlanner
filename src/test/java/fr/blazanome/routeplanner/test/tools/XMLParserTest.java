package fr.blazanome.routeplanner.test.tools;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fr.blazanome.routeplanner.model.Map;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLParser;

public class XMLParserTest {

    @Test
    public void testParse() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTest.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 4);
        assertEquals(segments.size(), 3);

        Intersection intersection = new Intersection(45.74979, 4.87572);
        assertEquals(intersection, intersections.get(0));

        intersection = new Intersection(45.754536, 4.866525);
        assertEquals(intersection, intersections.get(1));

        intersection = new Intersection(45.751904, 4.857877);
        assertEquals(intersection, intersections.get(2));

        intersection = new Intersection(45.75171, 4.8718166);
        assertEquals(intersection, intersections.get(3));

        Segment segment = new Segment(1, 64.89446, "Rue Claudius Penet", 0);
        assertEquals(segment, segments.get(0));

        segment = new Segment(2, 153.72511, "Rue Feuillat", 1);
        assertEquals(segment, segments.get(1));

        segment = new Segment(3, 91.81539, "Avenue Lacassagne", 2);
        assertEquals(segment, segments.get(2));
    }

    @Test
    public void testParserEmptyMap() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyMap.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserEmptyXML() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyXML.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserWrongElement1() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement1.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserWrongElement2() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement2.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserIntersectionsOnly() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestIntersectionsOnly.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 4);
        assertEquals(segments.size(), 0);

        Intersection intersection = intersections.get(0);
        assertEquals(intersection.getLatitude(), 45.74979);
        assertEquals(intersection.getLongitude(), 4.87572);

        intersection = intersections.get(1);
        assertEquals(intersection.getLatitude(), 45.754536);
        assertEquals(intersection.getLongitude(), 4.866525);

        intersection = intersections.get(2);
        assertEquals(intersection.getLatitude(), 45.751904);
        assertEquals(intersection.getLongitude(), 4.857877);

        intersection = intersections.get(3);
        assertEquals(intersection.getLatitude(), 45.75171);
        assertEquals(intersection.getLongitude(), 4.8718166);
    }

    @Test
    public void testParserSegmentsOnly() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestSegmentsOnly.xml");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        Iterator<Intersection> iterIntersections = map.getIntersections();
        List<Intersection> intersections = new ArrayList<>();
        iterIntersections.forEachRemaining(intersections::add);

        Iterator<Segment> iterSegments = map.getSegments();
        List<Segment> segments = new ArrayList<>();
        iterSegments.forEachRemaining(segments::add);

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 3);

        Segment segment = segments.get(0);
        assertEquals(segment.getDestination(), 1);
        assertEquals(segment.getLength(), 64.89446);
        assertEquals(segment.getName(), "Rue Claudius Penet");
        assertEquals(segment.getOrigin(), 0);

        segment = segments.get(1);
        assertEquals(segment.getDestination(), 2);
        assertEquals(segment.getLength(), 153.72511);
        assertEquals(segment.getName(), "Rue Feuillat");
        assertEquals(segment.getOrigin(), 1);

        segment = segments.get(2);
        assertEquals(segment.getDestination(), 3);
        assertEquals(segment.getLength(), 91.81539);
        assertEquals(segment.getName(), "Avenue Lacassagne");
        assertEquals(segment.getOrigin(), 2);
    }
}
