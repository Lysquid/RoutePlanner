package fr.blazanome.routeplanner.algorithm;

import java.util.ArrayList;
import java.util.function.Consumer;

import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Route;
import javafx.application.Platform;

/**
 * A CourierRouteUpdater which update the route each time the algorithm find
 * a new best solution
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
        var requests = new ArrayList<>(courier.getRequests());
        this.spawnTask(courier, () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }

            Route route = this.tourGenerationAlgorithm.computeTour(map, requests,
                    tmpRoute -> Platform.runLater(() -> courier.setRoute(tmpRoute)));
            Platform.runLater(() -> courier.setRoute(route));
        });
    }

}
