package fr.blazanome.routeplanner.test.tools;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fr.blazanome.routeplanner.model.Map;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLParser;
import jdk.jfr.Timestamp;


public class XMLParserTest {

    @Test
    public void testParse() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTest.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

        assertEquals(intersections.size(), 4);
        assertEquals(segments.size(), 3);

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

    @Test
    public void testParserEmptyMap() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyMap.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserEmptyXML() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyXML.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserWrongElement1() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement1.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserWrongElement2() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement2.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

        assertEquals(intersections.size(), 0);
        assertEquals(segments.size(), 0);
    }

    @Test
    public void testParserIntersectionsOnly() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestIntersectionsOnly.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

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
        xmlParser.parse("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestSegmentsOnly.xml");

        Map map = mapHandler.getMap();

        List<Intersection> intersections = map.getIntersections();
        List<Segment> segments = map.getSegments();

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
