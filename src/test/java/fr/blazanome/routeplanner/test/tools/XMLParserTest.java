package fr.blazanome.routeplanner.test.tools;

import fr.blazanome.routeplanner.model.AdjacencyListMap;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.model.map.MapBuilder;
import fr.blazanome.routeplanner.model.map.MapBuilderFactory;
import fr.blazanome.routeplanner.tools.XMLMapParser;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;

public class XMLParserTest {

    List<Intersection> addedIntersections;
    List<Segment> addedSegments;
    class Warehouse {
        public Intersection warehouse;
    }

    class TestMapBuilderFactory implements MapBuilderFactory {
        List<Intersection> intersections;
        List<Segment> segments;
        Warehouse warehouse;


        public TestMapBuilderFactory(List<Intersection> intersections, List<Segment> segments, Warehouse warehouse) {
            this.intersections = intersections;
            this.segments = segments;
            this.warehouse = warehouse;
        }

        @Override
        public MapBuilder createBuilder() {
            return new Builder(this.intersections, this.segments, this.warehouse);
        }
    }

    class Builder implements MapBuilder {

        List<Intersection> intersections;
        List<Segment> segments;
        Warehouse warehouse;

        public Builder(List<Intersection> intersections, List<Segment> segments, Warehouse warehouse) {
            this.intersections = intersections;
            this.segments = segments;
            this.warehouse = warehouse;
        }

        @Override
        public void addIntersection(Intersection intersection) {
            intersections.add(intersection);
        }

        @Override
        public void addSegment(Segment segment) {
            segments.add(segment);
        }

        @Override
        public void setWarehouse(Intersection intersection) {
            this.warehouse.warehouse = intersection;
        }

        @Override
        public IMap build() {
            return null;
        }
    }

    @Test
    public void testParse() throws ParserConfigurationException, IOException, SAXException {

        List<Intersection> intersections = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(intersections, segments, warehouse);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);
        IMap map = xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTest.xml"));

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

        assertEquals(expectedIntersections.get(0), warehouse.warehouse);
    }

    @Test
    public void testParserEmptyMap() throws ParserConfigurationException, IOException, SAXException {
        List<Intersection> intersections = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(intersections, segments, warehouse);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);
        IMap map = xmlParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyMap.xml"));

        assertTrue(intersections.isEmpty());
        assertTrue(segments.isEmpty());
        assertNull(warehouse.warehouse);
    }

    @Test
    public void testParserEmptyXML() throws ParserConfigurationException, IOException {

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(null, null, null);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);

        assertThrows(SAXException.class, () -> xmlParser
                .parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestEmptyXML.xml")));

    }

    @Test
    public void testParserWrongElement1() throws ParserConfigurationException, IOException, SAXException {
        List<Intersection> intersections = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(intersections, segments, warehouse);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);
        IMap map = xmlParser
                .parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement1.xml"));

        assertTrue(intersections.isEmpty());
        assertTrue(segments.isEmpty());
        assertNull(warehouse.warehouse);
    }

    @Test
    public void testParserWrongElement2() throws ParserConfigurationException, IOException, SAXException {
        List<Intersection> intersections = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(intersections, segments, warehouse);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);
        IMap map = xmlParser
                .parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestWrongElement2.xml"));

        assertTrue(intersections.isEmpty());
        assertTrue(segments.isEmpty());
        assertNull(warehouse.warehouse);
    }

    @Test
    public void testParserIntersectionsOnly() throws ParserConfigurationException, IOException, SAXException {
        List<Intersection> intersections = new ArrayList<>();
        List<Segment> segments = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(intersections, segments, warehouse);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);
        IMap map = xmlParser.parse(
                new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestIntersectionsOnly.xml"));

        assertThat(intersections).hasSameElementsAs(Arrays.asList(
                new Intersection(45.74979, 4.87572),
                new Intersection(45.754536, 4.866525),
                new Intersection(45.751904, 4.857877),
                new Intersection(45.75171, 4.8718166)));

        assertTrue(segments.isEmpty());
        assertEquals(new Intersection(45.74979, 4.87572), warehouse.warehouse);
    }

    @Test
    public void testParserSegmentsOnly() throws ParserConfigurationException, IOException, SAXException {
        MapBuilderFactory mapBuilderFactory = new TestMapBuilderFactory(null, null, null);
        XMLMapParser xmlParser = new XMLMapParser(mapBuilderFactory);

        assertThrows(RuntimeException.class, () -> xmlParser
                .parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/XMLParserTestSegmentsOnly.xml")));
    }
}
