package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.Intersection;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ButtonIntersection extends Button {

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

    public void setColor(Color color) {
        this.setStyle(String.format("-fx-background-color: #%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255))
        );
    }
}
