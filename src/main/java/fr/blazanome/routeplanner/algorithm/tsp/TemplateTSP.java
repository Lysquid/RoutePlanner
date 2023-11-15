package fr.blazanome.routeplanner.algorithm.tsp;

import fr.blazanome.routeplanner.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public abstract class TemplateTSP implements TSP {
    private List<Integer> bestSol;
    protected Graph graph;
    private double bestSolCost;
    private int timeLimit;
    private long startTime;
    private boolean interrupted;

    public void searchSolution(int timeLimit, Graph g, Consumer<List<Integer>> onNewRoute) {
        if (timeLimit <= 0)
            return;

        this.startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.graph = g;
        this.bestSol = null;
        this.interrupted = false;

        Collection<Integer> unvisited = new HashSet<>(g.getVerticesCount() - 1);
        for (int i = 1; i < g.getVerticesCount(); i++)
            unvisited.add(i);
        List<Integer> visited = new ArrayList<>(g.getVerticesCount());
        visited.add(0); // The first visited vertex is 0
        this.bestSolCost = Integer.MAX_VALUE;
        branchAndBound(0, unvisited, visited, 0, onNewRoute);
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
    protected abstract double bound(Integer currentVertex, Collection<Integer> unvisited);

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
            List<Integer> visited, double currentCost, Consumer<List<Integer>> onNewRoute) {
        if (System.currentTimeMillis() - startTime > timeLimit || Thread.interrupted()) {
            this.interrupted = true;
        }

        if (this.interrupted) {
                return;
        }

        if (unvisited.size() == 0) {
            if (graph.isArc(currentVertex, 0)) {
                if (currentCost + graph.getCost(currentVertex, 0) < bestSolCost) {
                    this.bestSol = new ArrayList<>(visited);
                    onNewRoute.accept(this.bestSol);
                    this.bestSolCost = currentCost + graph.getCost(currentVertex, 0);
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
            for (int vertex : this.unvisitedNeighbors(currentVertex, unvisited, graph)) {
                visited.add(vertex);
                unvisited.remove(vertex);
                branchAndBound(vertex, unvisited, visited,
                        currentCost + this.graph.getCost(currentVertex, vertex), onNewRoute);
                visited.remove(visited.size() - 1);
                unvisited.add(vertex);
            }
        }
    }

}
