package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.Intersection;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ButtonIntersection extends Button {

    private static final int RADIUS = 5;
    private static final Circle circle = new Circle(RADIUS);
    private final Intersection intersection;

    /**
     * Creates a button with a set size and that react to events
     *
     * @param mapView      The map where the intersection has to be drawn
     * @param intersection The information about the intersection used to create the button
     */
    public ButtonIntersection(MapView mapView, Intersection intersection) {
        this.intersection = intersection;

        this.setShape(ButtonIntersection.circle);
        this.setMaxSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);
        this.setMinSize(2 * ButtonIntersection.RADIUS, 2 * ButtonIntersection.RADIUS);

        this.setOnAction(event -> {
            mapView.onActionProperty().get().handle(event);
        });
    }

    /**
     * @return the intersection linked to the button
     */
    public Intersection getIntersection() {
        return intersection;
    }

    /**
     * updates the size of the button based on the zoom
     *
     * @param zoom Takes how much you're zoomed into the map
     */
    public void updateRadius(double zoom) {
        double radius = this.calculateRadius(zoom);
        this.circle.setRadius(radius);
        this.setShape(this.circle);
        this.setMaxSize(2 * radius, 2 * radius);
        this.setMinSize(2 * radius, 2 * radius);

    }

    /**
     * @param zoom Takes how much you're zoomed into the map
     * @return the radius that the button should have
     */
    public double calculateRadius(double zoom) {
        double radius = Math.min(this.RADIUS, 10.0 / zoom);
        return Math.max(radius, 1.0);
    }

    /**
     * @param color the color to set the button to
     */
    public void setColor(Color color) {
        this.setStyle(String.format("-fx-background-color: #%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255))
        );
    }
}
