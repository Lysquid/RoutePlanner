package fr.blazanome.routeplanner.test.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.blazanome.routeplanner.algorithm.tsp.TSP;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.graph.Graph;

/**
 * TPSTest
 */
public class TSPTest {

   @Test
   public void testTSP1() {
       Graph graph = TestGraphs.graph1();
       TSP tsp = new TSP1();
       tsp.searchSolution(Integer.MAX_VALUE, graph);

       assertEquals(Arrays.asList(0, 1, 4, 3, 5, 2), tsp.getSolution());
       assertEquals(37, tsp.getSolutionCost());
   }
}
