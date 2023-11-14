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
public class AnytimeCourierRouteUpdater extends ThreadPoolCourierRouteUpdater {

    private TourGenerationAlgorithm tourGenerationAlgorithm;

    public AnytimeCourierRouteUpdater(Consumer<Integer> onTaskCountChange,
            TourGenerationAlgorithm tourGenerationAlgorithm) {
        super(onTaskCountChange);
        this.tourGenerationAlgorithm = tourGenerationAlgorithm;
    }

    @Override
    public void updateTour(Courier courier, IMap map) {
        this.spawnTask(courier, () -> {
            Route route = this.tourGenerationAlgorithm.computeTour(map, courier.getRequests(),
                    tmpRoute -> Platform.runLater(() -> courier.setRoute(tmpRoute)));
            Platform.runLater(() -> courier.setRoute(route));
        });
    }

}
