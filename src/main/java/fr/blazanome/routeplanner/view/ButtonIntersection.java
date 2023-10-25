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
    private final Controller controller;
    private final Intersection intersection;

    public ButtonIntersection(Controller controller, Intersection intersection) {
        this.controller = controller;
        this.intersection = intersection;

        this.setShape(ButtonIntersection.circle);
        this.setMaxSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);
        this.setMinSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);

        this.setOnAction(event -> {
            ButtonIntersection button = (ButtonIntersection) event.getSource();
            this.controller.selectIntersection(button.getIntersection());
        });
    }

    public Intersection getIntersection() {
        return intersection;
    }


}
