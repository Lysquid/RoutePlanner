package fr.blazanome.routeplanner.graph;

/**
 * SimpleAdjacencyListsGraph
 */
public class SimpleAdjacencyListsGraph extends AdjacencyLists<Neighbor> {
    public SimpleAdjacencyListsGraph() {
        super();
    }

    public SimpleAdjacencyListsGraph(int vertexCount) {
        super(vertexCount);
    }

    public void addEdge(int start, int end, double cost) {
        this.addEdge(start, new Neighbor(end, cost));
    }
}
