package fr.blazanome.routeplanner.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AdjacencyListPathGraph
 */
public class AdjacencyListPathGraph implements PathGraph {

    private List<List<Edge>> adjacencyLists;
    private Graph parent;

    public AdjacencyListPathGraph(Graph parent) {
        this.parent = parent;
        this.adjacencyLists = new ArrayList<>();
    }

    public AdjacencyListPathGraph(Graph parent, int vertexCount) {
        this.parent = parent;

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

    public void addEdge(int start, int end, double cost, List<Integer> path) {
        assert start < this.adjacencyLists.size();
        assert end < this.adjacencyLists.size();

        this.adjacencyLists.get(start).add(new Edge(end, cost, path));
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
        assert start < this.adjacencyLists.size();
        assert end < this.adjacencyLists.size();

        var neighbors = this.adjacencyLists.get(start);
        for (Edge e : neighbors) {
            if (e.getDestination() == end) {
                return e.getCost();
            }
        }

        return -1.0;
    }

    @Override
    public Graph getParentGraph() {
        return this.parent;
    }

    @Override
    public Iterable<Integer> getPath(int start, int end) {
        assert start < this.adjacencyLists.size();
        assert end < this.adjacencyLists.size();

        var neighbors = this.adjacencyLists.get(start);
        for (Edge e : neighbors) {
            if (e.getDestination() == end) {
                return e.getPath();
            }
        }

        return null;
    }

    private class NeighborIterator implements Iterator<Neighbor> {
        private Iterator<Edge> edgeIterator;

        public NeighborIterator(Iterator<Edge> edgeIterator) {
            this.edgeIterator = edgeIterator;
        }

        @Override
        public boolean hasNext() {
            return this.edgeIterator.hasNext();
        }

        @Override
        public Neighbor next() {
            Edge edge = this.edgeIterator.next();
            return new Neighbor(edge.getDestination(), edge.getCost());
        }
    }

    private class Edge {
        private int destination;
        private double cost;
        private List<Integer> path;

        public Edge(int destination, double cost, List<Integer> path) {
            this.destination = destination;
            this.cost = cost;
            this.path = path;
        }

        public int getDestination() {
            return this.destination;
        }

        public double getCost() {
            return this.cost;
        }

        public List<Integer> getPath() {
            return this.path;
        }

    }
}
