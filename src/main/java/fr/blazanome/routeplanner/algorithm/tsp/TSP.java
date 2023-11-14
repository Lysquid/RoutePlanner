package fr.blazanome.routeplanner.algorithm.tsp;

import fr.blazanome.routeplanner.graph.Graph;

import java.util.List;
import java.util.function.Consumer;

public interface TSP {
	/**
	 * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
	 * (returns the best found tour whenever the time limit is reached)
	 * Warning: The computed tour always start from vertex 0
	 * @param timeLimit
     * @param onNewRoute function to call when a new Route is found
	 * @param g
	 */
	void searchSolution(int timeLimit, Graph g, Consumer<List<Integer>> onNewRoute);
	
	/**
	 * @return 
	 */
	List<Integer> getSolution();
	
	/** 
	 * @return the total cost of the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet).
	 */
	double getSolutionCost();

    public static interface Factory {
        TSP build();
    }
}
