package fr.blazanome.routeplanner.test.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.tools.MapHandler;
import fr.blazanome.routeplanner.tools.XMLParser;

import static org.assertj.core.api.Assertions.*;

public class XMLParserTest {

    @Test
    public void testParse() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTest.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();

        List<Intersection> intersections = StreamSupport.stream(map.getIntersections().spliterator(), false)
                .toList();

        List<Segment> segments = StreamSupport.stream(map.getSegments().spliterator(), false)
                .toList();

        List<Intersection> expectedIntersections = Arrays.asList(
                new Intersection(45.74979, 4.87572),
                new Intersection(45.754536, 4.866525),
                new Intersection(45.751904, 4.857877),
                new Intersection(45.75171, 4.8718166));
        assertThat(intersections).hasSameElementsAs(expectedIntersections);

        assertThat(segments).hasSameElementsAs(Arrays.asList(
                new Segment(expectedIntersections.get(1), 64.89446, "Rue Claudius Penet", expectedIntersections.get(0)),
                new Segment(expectedIntersections.get(2), 153.72511, "Rue Feuillat", expectedIntersections.get(1)),
                new Segment(expectedIntersections.get(3), 91.81539, "Avenue Lacassagne",
                        expectedIntersections.get(2))));
    }

    @Test
    public void testParserEmptyMap() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyMap.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
    }

    @Test
    public void testParserEmptyXML() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyXML.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
        }

        IMap map = mapHandler.getMap();
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
    }

    @Test
    public void testParserWrongElement1() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement1.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
    }

    @Test
    public void testParserWrongElement2() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement2.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
        assertEquals(0, StreamSupport.stream(map.getIntersections().spliterator(), false).count());
    }

    @Test
    public void testParserIntersectionsOnly() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestIntersectionsOnly.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        IMap map = mapHandler.getMap();
        List<Intersection> intersections = StreamSupport.stream(map.getIntersections().spliterator(), false)
                .toList();

        assertThat(intersections).hasSameElementsAs(Arrays.asList(
                new Intersection(45.74979, 4.87572),
                new Intersection(45.754536, 4.866525),
                new Intersection(45.751904, 4.857877),
                new Intersection(45.75171, 4.8718166)));

        assertEquals(0, StreamSupport.stream(map.getSegments().spliterator(), false).count());
    }

    @Test
    public void testParserSegmentsOnly() {
        MapHandler mapHandler = new MapHandler();
        XMLParser xmlParser = new XMLParser(mapHandler);
        try {
            xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestSegmentsOnly.xml"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        assertThrows(RuntimeException.class, mapHandler::getMap);
    }
}
