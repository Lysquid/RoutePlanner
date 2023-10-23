package fr.blazanome.routeplanner.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * AdjacencyList
 */
public class AdjacencyLists<EdgeType extends Neighbor> implements Graph {
    private List<List<EdgeType>> adjacencyLists;
    
    public AdjacencyLists() {
        this.adjacencyLists = new ArrayList<>();
    }

    public AdjacencyLists(int vertexCount) {
        this.adjacencyLists = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            this.adjacencyLists.add(new ArrayList<>());
        }
    }

    
    public int addVertex() {
        int id = this.adjacencyLists.size();
        this.adjacencyLists.add(new ArrayList<>());
        return id;
    }

    public void addEdge(int start, EdgeType edge) {
        assert start < this.adjacencyLists.size();
        assert edge.getVertex() < this.adjacencyLists.size();

        this.adjacencyLists.get(start).add(edge);
    }

    @Override
    public int getVerticesCount() {
        return this.adjacencyLists.size();
    }

    @Override
    public Iterable<Neighbor> getNeighbors(int vertex) {
        assert vertex < this.adjacencyLists.size();

        return () -> new NeighborIterator(this.adjacencyLists.get(vertex).iterator());
    }

    @Override
    public double getCost(int start, int end) {
        EdgeType customNeighbor = this.getCustomNeighbor(start, end);
        return customNeighbor != null ? customNeighbor.getCost() : -1.0; 
    }

    public Iterable<EdgeType> getEdges() {
        return () -> this.adjacencyLists.stream()
            .flatMap(l -> l.stream())
            .iterator();
    }

    public EdgeType getCustomNeighbor(int start, int end) {
        assert start < this.adjacencyLists.size();
        assert end < this.adjacencyLists.size();

        var neighbors = this.adjacencyLists.get(start);
        for (EdgeType e : neighbors) {
            if (e.getVertex() == end) {
                return e;
            }
        }

        return null;

    }

    private class NeighborIterator implements Iterator<Neighbor> {
        private Iterator<EdgeType> edgeIterator;

        public NeighborIterator(Iterator<EdgeType> edgeIterator) {
            this.edgeIterator = edgeIterator;
        }

        @Override
        public boolean hasNext() {
            return this.edgeIterator.hasNext();
        }

        @Override
        public Neighbor next() {
            EdgeType edge = this.edgeIterator.next();
            return (Neighbor) edge;
        }
    }
}
