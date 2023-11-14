package fr.blazanome.routeplanner.algorithm;

import java.util.function.Consumer;

import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Route;
import javafx.application.Platform;

/**
 * SimpleCourierRouteUpdater
 *
 * A CourierRouteUpdater which update the route only when the computation are
 * over
 */
public class SimpleCourierRouteUpdater extends ThreadPoolCourierRouteUpdater {

    private TourGenerationAlgorithm tourGenerationAlgorithm;

    public SimpleCourierRouteUpdater(Consumer<Integer> onTaskCountChange,
            TourGenerationAlgorithm tourGenerationAlgorithm) {
        super(onTaskCountChange);
        this.tourGenerationAlgorithm = tourGenerationAlgorithm;
    }

    @Override
    public void updateTour(Courier courier, IMap map) {
        this.spawnTask(courier, () -> {
            Route route = this.tourGenerationAlgorithm.computeTour(map, courier.getRequests(), null);


            System.out.println("End");
            Platform.runLater(() -> courier.setRoute(route));
        });
    }

}
