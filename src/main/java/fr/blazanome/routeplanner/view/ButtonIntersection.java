package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.Intersection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class ButtonIntersection extends ToggleButton {

    private static final int RADIUS = 5;
    private static final Shape circle = new Circle(RADIUS);
    private final Intersection intersection;

    public ButtonIntersection(MapView mapView, Intersection intersection) {
        this.intersection = intersection;

        this.setShape(ButtonIntersection.circle);
        this.setMaxSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);
        this.setMinSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);

        this.setOnAction(event -> {
            mapView.onActionProperty().get().handle(event);
        });
    }


    public Intersection getIntersection() {
        return intersection;
    }


}
