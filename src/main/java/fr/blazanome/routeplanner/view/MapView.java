package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.model.Session;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;

public class MapView extends Pane {

    private Session session; // reference kept to redraw on cursor drag, zoom, etc (no event from controller)
    private final Group root;
    double minX;
    double minY;
    double maxY;
    double maxX;
    private double initialX;
    private double initialY;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private final GraphicsContext gc;
    private final Canvas canvas;
    private List<ButtonIntersection> buttonIntersectionList;
    private final Scale zoomTransform;
    private double mouseX = 0.0;
    private double mouseY = 0.0;

    // sets up listeners and the canvas
    public MapView() {
        // sets up the canvas and updates for width
        this.canvas = new Canvas();
        this.gc = this.canvas.getGraphicsContext2D();
        this.root = new Group();
        this.getChildren().add(this.canvas);
        this.getChildren().add(this.root);

        Rectangle clippingRect = new Rectangle();
        clippingRect.heightProperty().bind(this.heightProperty());
        clippingRect.widthProperty().bind(this.widthProperty());
        this.setClip(clippingRect);

        this.widthProperty().addListener((observable, oldValue, newValue) -> draw());
        this.heightProperty().addListener((observable, oldValue, newValue) -> draw());

        // Handle mouse scroll events for zooming
        // Adds listener for zooming and dragging
        this.zoomTransform = new Scale(1.0, 1.0); // Initial scale is 1.0
        this.root.getTransforms().add(this.zoomTransform);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnScroll(event -> {
            double delta = event.getDeltaY(); // Positive for zoom in, negative for zoom out
            double scaleFactor = 1.05; // Adjust the zoom factor as needed
            if (delta > 0) {
                // Zoom in
                this.offsetX = this.offsetX - this.mouseX * (scaleFactor - 1) / this.zoomTransform.getX();
                this.offsetY = this.offsetY - this.mouseY * (scaleFactor - 1) / this.zoomTransform.getX();
                this.zoomTransform.setX(this.zoomTransform.getX() * scaleFactor);
                this.zoomTransform.setY(this.zoomTransform.getY() * scaleFactor);

            } else if (delta < 0) {
                // Zoom out
                this.offsetX = this.offsetX + this.mouseX * ((1 - (1 / scaleFactor))) / this.zoomTransform.getX();
                this.offsetY = this.offsetY + this.mouseY * ((1 - (1 / scaleFactor))) / this.zoomTransform.getX();
                this.zoomTransform.setX(this.zoomTransform.getX() / scaleFactor);
                this.zoomTransform.setY(this.zoomTransform.getY() / scaleFactor);
            }
            draw();
        });
        setOnMouseMoved(event -> {
            this.mouseX = event.getX();
            this.mouseY = event.getY();
        });
    }

    // General drawing section
    public void setUp(Session session) {

        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxY = 0;
        this.maxX = 0;

        this.session = session;

        if (this.buttonIntersectionList != null) {
            this.buttonIntersectionList.forEach(this.root.getChildren()::remove);
        }

        this.buttonIntersectionList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();

        for (Intersection intersection : session.getMap().getIntersections()) {
            // compute the bounds of the map to display all intersection
            this.minX = Math.min(this.minX, this.ConvertToX(intersection.getLatitude(), intersection.getLongitude()));
            this.minY = Math.min(this.minY, this.ConvertToY(intersection.getLatitude(), intersection.getLongitude()));
            this.maxX = Math.max(this.maxX, this.ConvertToX(intersection.getLatitude(), intersection.getLongitude()));
            this.maxY = Math.max(this.maxY, this.ConvertToY(intersection.getLatitude(), intersection.getLongitude()));

            // create the buttons representing the intersections
            ButtonIntersection button = new ButtonIntersection(this, intersection);
            if (intersection == session.getMap().getWarehouse()) {
                button.setId("warehouse");
            }
            toggleGroup.getToggles().add(button);
            this.root.getChildren().add(button);
            this.buttonIntersectionList.add(button);
        }
    }

    void draw() {

        double i = 0.0;
        double k = 1.0;

        this.canvas.setWidth(this.getWidth());
        this.canvas.setHeight(this.getHeight());

        // Re-draw everything on the canvas
        if (this.session != null) {
            // Checks that draw has been called then clears the canvas and redraws all the
            // relevant points
            this.gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            this.drawPlainSegments(this.session.getMap().getSegments());
            this.drawIntersections();
            // color changing

            // Normally the changing of the Colour would happen for every route, here I did
            // it on every step of the route to demonstrate how it works

            for (int courierId = this.session.getCouriers().size() - 1; courierId >= 0; courierId--) {
                Courier courier = this.session.getCouriers().get(courierId);
                if (courier.getRoute() != null) {
                    i = i + 1.0 / (double) session.getCouriers().size();
                    Color c = new Color(1 - i, i, i, 1.0);

                    this.drawRoute(courier.getRoute().getPath(), c);
                }
            }
        }
    }

    // Specific kinds of drawing

    void drawPlainSegments(Iterable<Segment> iterableSegment) {
        for (Segment segment : iterableSegment) {
            this.gc.setStroke(Color.BLACK);
            this.gc.setLineWidth(1.5);
            Intersection start = segment.getOrigin();
            Intersection end = segment.getDestination();

            // Calculate scaled positions for the segments
            double scaledX1 = this.positionX(start) * this.zoomTransform.getX();
            double scaledY1 = this.positionY(start) * this.zoomTransform.getY();
            double scaledX2 = this.positionX(end) * this.zoomTransform.getX();
            double scaledY2 = this.positionY(end) * this.zoomTransform.getY();

            // Draw the scaled segment
            this.gc.strokeLine(scaledX1, scaledY1, scaledX2, scaledY2);
        }

    }

    void drawRoute(Iterable<Segment> iterableSegment, Color c) {
        // the code should be very different from the other function once it's actually
        // implemented.

        // double personeCount=1.0;
        for (Segment segment : iterableSegment) {
            this.gc.setStroke(c);
            this.gc.setLineWidth(5);
            Intersection start = segment.getOrigin();
            Intersection end = segment.getDestination();
            // Calculate scaled positions for the segments
            double scaledX1 = this.positionX(start) * this.zoomTransform.getX();
            double scaledY1 = this.positionY(start) * this.zoomTransform.getY();
            double scaledX2 = this.positionX(end) * this.zoomTransform.getX();
            double scaledY2 = this.positionY(end) * this.zoomTransform.getY();
            // Draw the scaled segment
            gc.strokeLine(scaledX1, scaledY1, scaledX2, scaledY2);
        }
    }

    void drawIntersections() {
        int radius = 5;
        for (ButtonIntersection bt : this.buttonIntersectionList) {
            Intersection intersection = bt.getIntersection();
            double x = this.positionX(intersection) - radius;
            double y = this.positionY(intersection) - radius;
            bt.setLayoutX(x);
            bt.setLayoutY(y);
            bt.setVisible(true);
        }
    }

    // turns latitude and longitude into x and y coordinates

    double ConvertToX(double lat, double lon) {
        return Math.cos((Math.PI / 180.0) * lat) * 111.0 * lon;
    }

    double ConvertToY(double lat, double lon) {
        return 111.0 * lat;
    }

    // Calculate from all the x and y coordinates where to place the item on the
    // screen. Doesn't take into account zooming
    double positionX(Intersection intersection) {
        return this.ratioWidth()
                * (0.05 + 0.90 * ((this.ConvertToX(intersection.getLatitude(), intersection.getLongitude()) - this.minX)
                        / (this.maxX - this.minX)))
                + this.offsetX;
    }

    double positionY(Intersection intersection) {
        return this.ratioHeight()
                * (0.05 + 0.90 * ((this.ConvertToY(intersection.getLatitude(), intersection.getLongitude()) - this.minY)
                        / (this.maxY - this.minY)))
                + this.offsetY;

    }

    double ratioHeight() {
        return Math.min(this.getHeight(), this.getWidth() * (this.maxY - this.minY) / (this.maxX - this.minX));
    }

    double ratioWidth() {
        return Math.min(this.getWidth(), this.getHeight() * (this.maxX - this.minX) / (this.maxY - this.minY));
    }

    // Action to make the dragging possible
    private void handleMousePressed(MouseEvent event) {
        this.initialX = event.getSceneX();
        this.initialY = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) {
            double deltaX = event.getSceneX() - this.initialX;
            double deltaY = event.getSceneY() - this.initialY;
            this.offsetX = this.offsetX + deltaX / this.zoomTransform.getX();
            this.offsetY = this.offsetY + deltaY / this.zoomTransform.getY();
            // Iterate through child nodes and adjust their positions
            this.draw();

            this.initialX = event.getSceneX();
            this.initialY = event.getSceneY();
        }
    }

    // boilerplate stuff to be able to fire a custom onAction
    private final ObjectProperty<EventHandler<ActionEvent>> propertyOnAction = new SimpleObjectProperty<>();

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.propertyOnAction;
    }

    // says it's unused by getting rid of it seems to cause an error
    public final void setOnAction(EventHandler<ActionEvent> handler) {
        this.propertyOnAction.set(handler);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return this.propertyOnAction.get();

    }

}
