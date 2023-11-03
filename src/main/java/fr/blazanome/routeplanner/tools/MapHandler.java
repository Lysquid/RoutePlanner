package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.AdjacencyListMap;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MapHandler extends AbstractMapHandler {

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

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.elementValue == null) {
            this.elementValue = new StringBuilder();
        } else {
            this.elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        this.intersections = new java.util.HashMap<Long, Intersection>();
        this.segments = new java.util.ArrayList<ParserSegment>();
    }

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
                this.intersections.put(id, new Intersection(latitude, longitude));
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

    @Override
    public IMap getMap() {
        AdjacencyListMap map = new AdjacencyListMap();
        this.intersections.values().forEach(map::addIntersection);

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
                .forEach(map::addSegment);

        map.setWarehouse(this.intersections.get(this.warehouse));
        return map;
    }
}
