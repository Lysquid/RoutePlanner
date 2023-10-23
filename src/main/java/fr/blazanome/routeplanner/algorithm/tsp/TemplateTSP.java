package fr.blazanome.routeplanner.algorithm.tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import fr.blazanome.routeplanner.graph.Graph;

public abstract class TemplateTSP implements TSP {
    private List<Integer> bestSol;
    protected Graph graph;
    private double bestSolCost;
    private int timeLimit;
    private long startTime;

    public void searchSolution(int timeLimit, Graph g) {
        if (timeLimit <= 0)
            return;

        this.startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.graph = g;
        this.bestSol = null;

        Collection<Integer> unvisited = new HashSet<>(g.getVerticesCount() - 1);
        for (int i = 1; i < g.getVerticesCount(); i++)
            unvisited.add(i);
        List<Integer> visited = new ArrayList<>(g.getVerticesCount());
        visited.add(0); // The first visited vertex is 0
        this.bestSolCost = Integer.MAX_VALUE;
        branchAndBound(0, unvisited, visited, 0);
    }

    public List<Integer> getSolution() {
        return this.bestSol;
    }

    public double getSolutionCost() {
        if (this.graph != null)
            return this.bestSolCost;
        return -1;
    }

    /**
     * Method that must be defined in TemplateTSP subclasses
     * 
     * @param currentVertex
     * @param unvisited
     * @return a lower bound of the cost of paths in <code>g</code> starting from
     *         <code>currentVertex</code>, visiting
     *         every vertex in <code>unvisited</code> exactly once, and returning
     *         back to vertex <code>0</code>.
     */
    protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);

    /**
     * Method that must be defined in TemplateTSP subclasses
     * 
     * @param currentVertex
     * @param unvisited
     * @param g
     * @return an iterator for visiting all vertices in <code>unvisited</code> which
     *         are successors of <code>currentVertex</code>
     */
    protected abstract Iterable<Integer> unvisitedNeighbors(Integer currentVertex, Collection<Integer> unvisited,
            Graph g);

    /**
     * Template method of a branch and bound algorithm for solving the TSP in
     * <code>g</code>.
     * 
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @param visited       the sequence of vertices that have been already visited
     *                      (including currentVertex)
     * @param currentCost   the cost of the path corresponding to
     *                      <code>visited</code>
     */
    private void branchAndBound(int currentVertex, Collection<Integer> unvisited,
            List<Integer> visited, double currentCost) {
        if (System.currentTimeMillis() - startTime > timeLimit)
            return;
        if (unvisited.size() == 0) {
            if (graph.isArc(currentVertex, 0)) {
                if (currentCost + graph.getCost(currentVertex, 0) < bestSolCost) {
                    this.bestSol = new ArrayList<>(visited);
                    this.bestSolCost = currentCost + graph.getCost(currentVertex, 0);
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
            for (int vertex : this.unvisitedNeighbors(currentVertex, unvisited, graph)) {
                visited.add(vertex);
                unvisited.remove(vertex);
                branchAndBound(vertex, unvisited, visited,
                        currentCost + this.graph.getCost(currentVertex, vertex));
                visited.remove(visited.size() - 1);
                unvisited.add(vertex);
            }
        }
    }

}
