package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.model.map.MapBuilder;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SAX handler that builds a map
 */
public class MapHandler extends AbstractMapHandler {

    /**
     * Representation a parsed segment, uses indices given in the XML file
     */
    private class ParserSegment {
        private long destination;
        private double length;
        private String name;
        private long origin;

        public ParserSegment(long destination, double length, String name, long origin) {
            this.destination = destination;
            this.length = length;
            this.name = name;
            this.origin = origin;
        }

        public String getName() {
            return this.name;
        }
    }

    private static final String WAREHOUSE = "warehouse";
    private static final String INTERSECTION = "intersection";
    private static final String SEGMENT = "segment";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String ID = "id";
    private static final String LENGTH = "length";
    private static final String DESTINATION = "destination";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String ORIGIN = "origin";
    private StringBuilder elementValue;

    private java.util.Map<Long, Intersection> intersections;
    private java.util.List<ParserSegment> segments;

    private long warehouse;

    /**
     * Handle characters
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.elementValue == null) {
            this.elementValue = new StringBuilder();
        } else {
            this.elementValue.append(ch, start, length);
        }
    }

    /**
     * Initializes structures that will be used to gradually parse the map
     * @throws SAXException never throws, used to comply to SAX
     */
    @Override
    public void startDocument() throws SAXException {
        this.intersections = new java.util.HashMap<Long, Intersection>();
        this.segments = new java.util.ArrayList<ParserSegment>();
    }

    /**
     * Handles when an element start is detected in the document
     * @param uri the identifier of the element (unused)
     * @param lName local (namespace) name
     * @param qName qualified name of the element
     * @param attr attributes of the element
     * @throws SAXException never throws, used to comply to SAX
     */
    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case WAREHOUSE:
                this.warehouse = Long.parseLong(attr.getValue(ADDRESS));
                break;
            case INTERSECTION:
                long id = Long.parseLong(attr.getValue(ID));
                double latitude = Double.parseDouble(attr.getValue(LATITUDE));
                double longitude = Double.parseDouble(attr.getValue(LONGITUDE));
                this.intersections.put(id, new Intersection(id, latitude, longitude));
                break;
            case SEGMENT:
                long destination = Long.parseLong(attr.getValue(DESTINATION));
                double length = Double.parseDouble(attr.getValue(LENGTH));
                String name = attr.getValue(NAME);
                long origin = Long.parseLong(attr.getValue(ORIGIN));
                this.segments.add(new ParserSegment(destination, length, name, origin));
                break;
        }
    }

    /**
     * Uses the mapBuilder to construct the map with the data parsed in the document
     * @param builder the MapBuilder used to build the map
     */
    @Override
    public void buildMap(MapBuilder builder) {
        this.intersections.values().forEach(builder::addIntersection);

        this.segments.stream().map(parser_segment -> {
            if (!this.intersections.containsKey(parser_segment.destination)
                    || !this.intersections.containsKey(parser_segment.origin)) {
                throw new RuntimeException("Invalid map");
            }
            return new Segment(
                    this.intersections.get(parser_segment.destination),
                    parser_segment.length,
                    parser_segment.name,
                    this.intersections.get(parser_segment.origin));
        })
                .forEach(builder::addSegment);

        builder.setWarehouse(this.intersections.get(this.warehouse));
    }
}
