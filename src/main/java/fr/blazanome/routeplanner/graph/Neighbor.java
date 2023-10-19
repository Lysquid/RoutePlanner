package fr.blazanome.routeplanner.graph;

/**
 * Neighbor
 * Represents the neighbor from another vertex in a {@link Graph}
 * It contains the neighbor vertex and the cost of the edge between the two
 * vertices
 */
public class Neighbor {

    private int vertex;
    private double cost;

    public Neighbor(int vertex, double cost) {
        this.vertex = vertex;
        this.cost = cost;
    }

    public int getVertex() {
        return this.vertex;
    }

    public double getCost() {
        return this.cost;
    }
}
