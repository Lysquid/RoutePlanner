package fr.blazanome.routeplanner.graph;

/**
 * Neighbor
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
