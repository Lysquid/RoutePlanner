package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.MapLoadedState;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.observer.Observable;
import fr.blazanome.routeplanner.observer.Observer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.input.MouseEvent;


public class Map extends Pane implements Observer {
    boolean drawn = false;

    Controller controller;
    private Group root = new Group();
    Rectangle warehouseItem;
    private Button active = null;
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxY = 0;
    double maxX = 0;
    private double initialX;
    private double initialY;
    private boolean isDragging = false;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    GraphicsContext gc;
    Canvas canvas;
    ArrayList<Button> buttonIntersectionList = new ArrayList<>();
    HashMap<Button, Intersection> buttonIntersection = new HashMap<>();
    Scale zoomTransform = new Scale(1.0, 1.0); // Initial scale is 1.0

    //sets up listeners and the canvas
    public Map() {
        //sets up the canvas and updates for wdith
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
        widthProperty().addListener((observable, oldValue, newValue) -> redraw());
        heightProperty().addListener((observable, oldValue, newValue) -> redraw());       // Handle mouse scroll events for zooming
        // Adds listener for zooming and dragging
        root.getTransforms().add(zoomTransform);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);
        setOnScroll(event -> {
            double delta = event.getDeltaY(); // Positive for zoom in, negative for zoom out
            double scaleFactor = 1.05; // Adjust the zoom factor as needed
            if (delta > 0) {
                // Zoom in
                zoomTransform.setX(zoomTransform.getX() * scaleFactor);
                zoomTransform.setY(zoomTransform.getY() * scaleFactor);
            } else {
                // Zoom out
                zoomTransform.setX(zoomTransform.getX() / scaleFactor);
                zoomTransform.setY(zoomTransform.getY() / scaleFactor);
            }
            redraw();
        });
    }

    //General drawing section

    public void setController(Controller controller) {
        this.controller = controller;
        this.controller.addObserver(this);
    }
    void draw() {
        //so that redraws don't happens before a first drawing is done
        drawn = true;
        //sets up the canvas and finds the edges of the wanted map
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        IMap map = controller.getMap();
        Iterable<Intersection> iterableIntersection = map.getIntersections();
        for (Intersection intersection : iterableIntersection) {
            minX = Math.min(minX, ConvertToX(intersection.getLatitude(), intersection.getLongitude()));
            minY = Math.min(minY, ConvertToY(intersection.getLatitude(), intersection.getLongitude()));
            maxX = Math.max(maxX, ConvertToX(intersection.getLatitude(), intersection.getLongitude()));
            maxY = Math.max(maxY, ConvertToY(intersection.getLatitude(), intersection.getLongitude()));
        }
        //calls the drawing functions
        this.drawPlainSegments(map.getSegments());
        this.drawIntersections(iterableIntersection);
        this.drawRoute(this.controller.getCurrentPath());
        this.drawWarehouse(map.getWarehouse());

        this.getChildren().add(root);
    }

    void redraw() {
        //Checks that draw has been called then clears the canvas and redraws all the relevant points
        if (drawn) {
            canvas.setWidth(getWidth());
            canvas.setHeight(getHeight());
            // Re-draw everything on the canvas
            IMap map = this.controller.getMap();
            gc.clearRect(0, 0, getWidth(), getHeight());
            this.drawPlainSegments(map.getSegments());
            this.updateIntersections();
            this.drawRoute(this.controller.getCurrentPath());
            this.updateWarehouse();
        }

    }

    //Specific kinds of drawing

    void drawPlainSegments(Iterable<Segment> iterableSegment) {
        for (Segment segment : iterableSegment) {
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            Intersection start = segment.getOrigin();
            Intersection end = segment.getDestination();

            // Calculate scaled positions for the segments
            double scaledX1 = positionX(start) * zoomTransform.getX();
            double scaledY1 = positionY(start) * zoomTransform.getY();
            double scaledX2 = positionX(end) * zoomTransform.getX();
            double scaledY2 = positionY(end) * zoomTransform.getY();

            // Draw the scaled segment
            gc.strokeLine(scaledX1, scaledY1, scaledX2, scaledY2);
        }

    }

    void drawRoute(Iterable<Segment> iterableSegment) {
        //the code should be very different from the other function once it's actually implemented.
        double i = 0.0;
        //double personeCount=1.0;
        for (Segment segment : iterableSegment) {
            //i=i+1.0/personeCount;
            //Normally the changing of the Colour would happen for every route, here I did it on every step of the route to demonstrate how it works
            Color c = new Color(1 - i, i, i, 1.0);
            gc.setStroke(c);
            gc.setLineWidth(5);
            Intersection start = segment.getOrigin();
            Intersection end = segment.getDestination();
            // Calculate scaled positions for the segments
            double scaledX1 = positionX(start) * zoomTransform.getX();
            double scaledY1 = positionY(start) * zoomTransform.getY();
            double scaledX2 = positionX(end) * zoomTransform.getX();
            double scaledY2 = positionY(end) * zoomTransform.getY();
            // Draw the scaled segment
            gc.strokeLine(scaledX1, scaledY1, scaledX2, scaledY2);
        }
    }

    void drawIntersections(Iterable<Intersection> iterableIntersection) {
        EventHandler<ActionEvent> event = e -> {
            Button clicked = (Button) e.getSource();
            Intersection selectedintersection = buttonIntersection.get(clicked);
            this.controller.selectIntersection(selectedintersection,clicked);
        };
        int radius = 5;
        Circle c = new Circle(radius);
        for (Intersection intersection : iterableIntersection) {
            Button bt = new Button();
            bt.setShape(c);
            bt.setMaxSize(2 * radius, 2 * radius);
            bt.setMinSize(2 * radius, 2 * radius);
            bt.setLayoutX(positionX(intersection) - radius);
            bt.setLayoutY(positionY(intersection) - radius);
            bt.setOnAction(event);
            root.getChildren().add(bt);
            buttonIntersection.put(bt, intersection);
            buttonIntersectionList.add(bt);
        }

    }

    void updateIntersections() {
        int radius = 5;
        for (Button bt : buttonIntersectionList) {
            Intersection intersection = buttonIntersection.get(bt);
            double x = positionX(intersection) - radius;
            double y = positionY(intersection) - radius;
            bt.setLayoutX(x);
            bt.setLayoutY(y);
            // if a button comme out of the image it becomes invisible and can't be interacted with
            bt.setVisible(!(x < -radius) && !(y < -radius));
            bt.setDisable((x < -radius) || (y < -radius));
        }

    }

    void drawWarehouse(Intersection warehouseLocation) {
        warehouseItem = new Rectangle(8, 8);
        warehouseItem.setLayoutX(positionX(warehouseLocation));
        warehouseItem.setLayoutY(positionY(warehouseLocation));
        root.getChildren().add(warehouseItem);
    }

    void updateWarehouse() {
        IMap map = this.controller.getMap();
        double x = positionX(map.getWarehouse());
        double y = positionY(map.getWarehouse());
        warehouseItem.setLayoutX(x);
        warehouseItem.setLayoutY(y);
        warehouseItem.setVisible(!(x < -0) && !(y < -0));
    }

    //turns latitude and longitude into x and y coordinates

    double ConvertToX(double lat, double lon) {
        return Math.cos((Math.PI / 180.0) * lat) * 111.0 * lon;
    }

    double ConvertToY(double lat, double lon) {
        return 111.0 * lat;
    }

    //Calculate from all the x and y coordinates where to place the item on the screen. Doesn't take into account zooming
    double positionX(Intersection intersection) {
        return ratioWidth() * (0.05 + 0.90 * ((ConvertToX(intersection.getLatitude(), intersection.getLongitude()) - minX) / (maxX - minX))) + offsetX/zoomTransform.getX();
    }

    double positionY(Intersection intersection) {
        return ratioHeight()* (0.05 + 0.90 * ((ConvertToY(intersection.getLatitude(), intersection.getLongitude()) - minY) / (maxY - minY))) + offsetY/zoomTransform.getY();

    }
    double ratioHeight(){
        return Math.min(this.getHeight(),this.getWidth()*(maxY-minY)/(maxX-minX));
    }
    double ratioWidth(){
        return Math.min(this.getWidth(),this.getHeight()*(maxX-minX)/(maxY-minY));
    }

    //Action to make the dragging possible
    private void handleMousePressed(MouseEvent event) {
        initialX = event.getSceneX();
        initialY = event.getSceneY();
        isDragging = true;
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isDragging) {
            double deltaX = event.getSceneX() - initialX;
            double deltaY = event.getSceneY() - initialY;
            offsetX = offsetX + deltaX;
            offsetY = offsetY + deltaY;
            // Iterate through child nodes and adjust their positions
            redraw();

            initialX = event.getSceneX();
            initialY = event.getSceneY();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        isDragging = false;
    }

    @Override
    public void update(Observable observable, Object message) {
        if (message instanceof MapLoadedState) {
            this.draw();
        }

        if (message == null) {
            this.redraw();
        }
    }

     
}

