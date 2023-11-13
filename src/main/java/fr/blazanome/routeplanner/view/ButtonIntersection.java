package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.Intersection;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.Circle;

public class ButtonIntersection extends ToggleButton {

    private static final int RADIUS = 5;
    private static final Circle circle = new Circle(RADIUS);
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
    public void updateRadius(double zoom) {
        double radius=this.calculateRadius(zoom);
        this.circle.setRadius(radius);
        this.setShape(this.circle);
        this.setMaxSize(2 * radius, 2 * radius);
        this.setMinSize(2 * radius, 2 * radius);

    }
    public double calculateRadius(double zoom){
        double radius=Math.min(this.RADIUS,10.0/zoom);
        return Math.max(radius,1.0);
    }

}
