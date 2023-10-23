package fr.blazanome.routeplanner.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import fr.blazanome.routeplanner.graph.AdjacencyLists;
import fr.blazanome.routeplanner.graph.Neighbor;

/**
 * AdjacencyListMap
 */
public class AdjacencyListMap extends AdjacencyLists<AdjacencyListMap.NeighborWithSegment> implements IMap {
    private Intersection warehouse;
    private Map<Intersection, Integer> vertexIdMap;
    private Map<Integer, Intersection> intersectionMap;

    public AdjacencyListMap() {
        this.vertexIdMap = new HashMap<>();
        this.intersectionMap = new HashMap<>();
        this.warehouse = null;
    }

    public void addIntersection(Intersection intersection) {
        int vertexId = this.addVertex();
        this.vertexIdMap.put(intersection, vertexId);
        this.intersectionMap.put(vertexId, intersection);
    }

    public void addSegment(Segment segment) {
        int start = this.getVertexId(segment.getOrigin());
        int end = this.getVertexId(segment.getDestination());

        assert start >= 0;
        assert end >= 0;

        this.addEdge(start, new AdjacencyListMap.NeighborWithSegment(end, segment.getLength(), segment));
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public Iterable<Segment> getSegments() {
        return () -> StreamSupport.stream(this.getEdges().spliterator(), false)
                .map(AdjacencyListMap.NeighborWithSegment::getSegment)
                .iterator();
    }

    @Override
    public Segment getSegment(int start, int end) {
        NeighborWithSegment customNeighbor = this.getCustomNeighbor(start, end);
        return customNeighbor != null ? customNeighbor.getSegment() : null;
    }

    @Override
    public Iterable<Intersection> getIntersections() {
        return this.intersectionMap.values();
    }

    @Override
    public Intersection getIntersection(int vertexId) {
        return this.intersectionMap.get(vertexId);
    }

    @Override
    public int getVertexId(Intersection intersection) {
        Integer id = this.vertexIdMap.get(intersection);
        return id == null ? -1 : id.intValue();
    }

    @Override
    public Intersection getWarehouse() {
        return this.warehouse;
    }

    protected static class NeighborWithSegment extends Neighbor {
        private Segment segment;

        public NeighborWithSegment(int vertex, double cost, Segment segment) {
            super(vertex, cost);
            this.segment = segment;
        }

        public Segment getSegment() {
            return segment;
        }
    }
}
