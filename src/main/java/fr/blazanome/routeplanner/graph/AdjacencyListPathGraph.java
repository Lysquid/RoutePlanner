package fr.blazanome.routeplanner.graph;

import java.util.List;

/**
 * AdjacencyListPathGraph
 */
public class AdjacencyListPathGraph extends AdjacencyLists<AdjacencyListPathGraph.NeighborWithPath>
        implements PathGraph {

    private Graph parent;

    public AdjacencyListPathGraph(Graph parent) {
        super();
        this.parent = parent;
    }

    public AdjacencyListPathGraph(Graph parent, int vertexCount) {
        super(vertexCount);
        this.parent = parent;
    }

    @Override
    public Graph getParentGraph() {
        return this.parent;
    }

    @Override
    public Iterable<Integer> getPath(int start, int end) {
        NeighborWithPath neighbor = this.getCustomNeighbor(start, end);
        return neighbor != null ? neighbor.getPath() : null;
    }

    public void addEdge(int start, int end, double cost, List<Integer> path) {
        this.addEdge(start, new AdjacencyListPathGraph.NeighborWithPath(end, cost, path));
    }

    public class NeighborWithPath extends Neighbor {
        private List<Integer> path;

        public NeighborWithPath(int destination, double cost, List<Integer> path) {
            super(destination, cost);
            this.path = path;
        }

        public List<Integer> getPath() {
            return this.path;
        }
    }
}
