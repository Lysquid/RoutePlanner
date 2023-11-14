package fr.blazanome.routeplanner.algorithm;

import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.IMap;

/**
 * CourierRouteUpdater
 * This represents an object capable of updating the Route of a Courier based on a map
 */
public interface CourierRouteUpdater {

    /**
     * This method update the courier route based on it's request
     *
     * Warning : The courier route should be set in the JavaFX thread
     * using Platform.runLater if necessary
     * @param courier
     * @param map
     */
    void updateTour(Courier courier, IMap map);     

    /**
     * Cancels all computation being run
     */
    void cancel();

    /** 
     * Cleanup any thread running to gracefully shutdown
     */
    void shutdown();
}
