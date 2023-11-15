package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.RequestSelectedState;
import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.controller.state.MapLoadedState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private Courier currentCourier;

    private static final Color[] DEFAULT_COLOR_LIST = {Color.RED , Color.BLUE , Color.MAGENTA , Color.CYAN ,
            Color.YELLOW  , Color.WHITE , Color.GRAY , Color.ORANGE , Color.PINK };
    /**
     * Constructor for MapView, sets up the items like root and canvas that store the elements of the map
     * The sets up the calls for functions on resizing and moving the map
     */
    // sets up listeners and the canvas
    public MapView() {
        currentCourier=null;
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
        setOnScroll(this::handleScroll);
        setOnMouseMoved(event -> {
            this.mouseX = event.getX();
            this.mouseY = event.getY();
        });
    }
    /**
     * Resets the intersection list so that it contains a button for every intersection
     * Calculates the edges of the map.
     * @param session Session stores the information about the map and user that needs to be highlighted
     */
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
                button.setDisable(true);
            }
            this.root.getChildren().add(button);
            this.buttonIntersectionList.add(button);
        }
    }
    /**
     * @param selectedCourier either the courier that is currently selected by the user or null depending on if all courier should be drawn
     */
    void setSelectedCourier(Courier selectedCourier){
        this.currentCourier = selectedCourier;
        this.draw();
    }
    /**
     * on a clear canvas draws the segments, the routes taken for the couriers or the selected courier and updates the intersections
     */
    void draw() {
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
            for (Courier courier : session.getCouriers()) {
                if (this.currentCourier == null || this.currentCourier == courier) {
                    Color c = this.getCourierColor(courier);
                    if (courier.getRoute() != null) {
                        this.drawRoute(courier.getRoute().getPath(), c);
                    }
                }
            }
            this.redrawIntersections();
        }
    }
    /**
     * @param controller controls the states
     * @param state   the current situation of the UI that changes what actions are possible. Is updated based upon general needs
     */
    public void onStateChange(Controller controller, State state) {
        if(state instanceof IntersectionSelectedState s) {
            this.selectIntersection(s.getSelectedIntersection());
        } else if(state instanceof RequestSelectedState s) {
            this.selectIntersection(s.getDeliveryRequest().getIntersection());
        } else if(state instanceof MapLoadedState s) {
            this.deselectIntersection();
        }

    }
    /**
     * Sets the Id to null for any selected intersection. And then redraws the intersections
     */
    private void deselectIntersection() {
        if(buttonIntersectionList == null) {
            return;
        }
        for (var b: this.buttonIntersectionList) {
            if(b.getId() != null && b.getId().equals("selected")) {
                b.setId(null);
            }
        }
        redrawIntersections();
    }
    /**
     * Sets the Id to null for any selected intersection. Adds a selected intersection and then redraws the intersections
     * @param intersection the intersection that will be added as selected
     */
    private void selectIntersection(Intersection intersection){
        if(buttonIntersectionList == null) {
            return;
        }
        for (var b: this.buttonIntersectionList) {
            if(b.getId() != null && b.getId().equals("selected")) {
                b.setId(null);
            }
            if(b.getIntersection().equals(intersection)) {
                b.setId("selected");
                b.setStyle(null);
            }
        }
        redrawIntersections();
    }

    /**
     * Colors intersections where a delivery is being done
     */
    private void redrawIntersections() {
        if(buttonIntersectionList == null) {
            return;
        }
        for(var b: this.buttonIntersectionList){
            if(b.getId() != null && (b.getId().equals("selected") || b.getId().equals("warehouse"))){
                continue;
            }
            b.setStyle(null);
            b.setId(null);
            for (Courier courier : session.getCouriers()) {
                if (this.currentCourier == null || this.currentCourier == courier) {
                    Color c = this.getCourierColor(courier);
                    for(var request: courier.getRequests()){
                        if(request.getIntersection().equals(b.getIntersection())) {
                            b.setColor(c);
                            b.setId("request");
                        }
                    }
                }
            }
        }
    }

    private Color getCourierColor(Courier courier){
        if (courier.getId()-1 < DEFAULT_COLOR_LIST.length) {
            return DEFAULT_COLOR_LIST[courier.getId()-1];
        } else {
            Random generator = new Random(courier.getId());
            return new Color(generator.nextDouble(), generator.nextDouble(), generator.nextDouble(), 1.0);
        }
    }

    // Specific kinds of drawing
    /**
     * Draws in black a line for each segment in the list
     * @param iterableSegment A list of segments, each have a start and end point for the line to use
     */
    void drawPlainSegments(Iterable<Segment> iterableSegment) {
        for (Segment segment : iterableSegment) {
            this.gc.setStroke(Color.BLACK);
            this.gc.setLineWidth(1.5*this.zoomTransform.getX());
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
    /**
     * Draws a line from point to point using the chosen colour and add lines such that it forms and arrow pointing towards the end point
     * @param iterableSegment A list of segments each have a start and end point
     * @param c the colour of the segment
     */
    void drawRoute(Iterable<Segment> iterableSegment, Color c) {
        //the code should be very different from the other function once it's actually implemented.
        //double personeCount=1.0;
        for (Segment segment : iterableSegment) {
            this.gc.setStroke(c);
            this.gc.setLineWidth(2.0*this.zoomTransform.getX());
            Intersection start = segment.getOrigin();
            Intersection end = segment.getDestination();
            // Calculate scaled positions for the segments
            double scaledX1 = this.positionX(start) * this.zoomTransform.getX();
            double scaledY1 = this.positionY(start) * this.zoomTransform.getY();
            double scaledX2 = this.positionX(end) * this.zoomTransform.getX();
            double scaledY2 = this.positionY(end) * this.zoomTransform.getY();
            double pente=(scaledX1-scaledX2)/(scaledY1-scaledY2);
            //calculates one of the two vectors that follows the line from the start to end and
            double dx=pente/(Math.sqrt(Math.pow(pente,2)+1.0));
            double dy=1/(Math.sqrt(Math.pow(pente,2)+1.0));
            //Finds the perpendicular vector by calculating it as (px 1) then normalizing it
            double perpendicularx=-dy/dx;
            double perpendiculary=1/(Math.sqrt(Math.pow(perpendicularx,2)+1.0));
            perpendicularx=perpendicularx/(Math.sqrt(Math.pow(perpendicularx,2)+1.0));
            gc.strokeLine(scaledX1, scaledY1, scaledX2, scaledY2 );



            double length=Math.sqrt(Math.pow(scaledX1-scaledX2,2)+Math.pow(scaledY1-scaledY2,2));
            double ratio=1.0/4.0;
            //This sets the size of the arrow and makes sure it resizes when we zoom in
            double arrow = 6 * Math.sqrt(this.zoomTransform.getX());
            //In this function we draw an arrow at the end point by tracing two lines from the end point to two other points.
            //These two points are found by going 6 units down the line then 6 units following a vector perpendicular to the line this gives two lines at a 45° angle on either side
            //The if exists because depending the circumstance the vector could be going either direction
            if((scaledX1<scaledX2 && scaledY1<scaledY2)||(scaledX1>scaledX2 && scaledY1<scaledY2)) {
                gc.strokeLine(scaledX2-(length*ratio)*dx, scaledY2-(length*ratio)*dy, scaledX2 + perpendicularx * arrow - dx * (arrow+length*ratio), scaledY2 + arrow * perpendiculary - (arrow+length*ratio) * dy);
                gc.strokeLine(scaledX2-(length*ratio)*dx, scaledY2-(length*ratio)*dy, scaledX2 - perpendicularx * arrow - dx * (arrow+length*ratio), scaledY2 - arrow * perpendiculary - (arrow+length*ratio) * dy);
            }
            else{
                gc.strokeLine(scaledX2+(length*ratio)*dx, scaledY2+(length*ratio)*dy, scaledX2 + perpendicularx * arrow + dx * (arrow+length*ratio), scaledY2 + arrow * perpendiculary + (arrow+length*ratio) * dy);
                gc.strokeLine(scaledX2+(length*ratio)*dx, scaledY2+(length*ratio)*dy, scaledX2 - perpendicularx * arrow + dx * (arrow+length*ratio), scaledY2 - arrow * perpendiculary + (arrow+length*ratio) * dy);
            }
            //gc.strokeLine(scaledX1, scaledY1, scaledX1, scaledY1);

        }
    }
    /**
     * Calculates the size and location of the buttons
     */
    void drawIntersections() {
        double radius =this.buttonIntersectionList.get(1).calculateRadius(this.zoomTransform.getX());
        for (ButtonIntersection bt : this.buttonIntersectionList) {
            Intersection intersection = bt.getIntersection();
            double x = this.positionX(intersection) - radius;
            double y = this.positionY(intersection) - radius;
            bt.setLayoutX(x);
            bt.setLayoutY(y);
            bt.setVisible(true);
        }
    }
    /**
    * Resets the zoom and position
     */
    void resetPosition(){
        this.offsetY=0;
        this.offsetX=0;
        this.zoomTransform.setX(1.0);
        this.zoomTransform.setY(1.0);
        this.resizeButton();
        this.draw();
    }
    // turns latitude and longitude into x and y coordinates
    /**
     * For each intersection calls a function that takes into account the level of zoom when defining the size of the button
     */
    void resizeButton(){
        if (buttonIntersectionList != null) {
            for (ButtonIntersection intersection : this.buttonIntersectionList) {
                intersection.updateRadius(this.zoomTransform.getX());
            }
        }
    }
    /**
     * @param lat latitude of the point
     * @param lon longitude of a point
     * @return the coordinates following the x axis
     */
    double ConvertToX(double lat, double lon) {
        return Math.cos((Math.PI / 180.0) * lat) * 111.0 * lon;
    }
    /**
     * @param lat latitude of the point
     * @param lon longitude of a point
     * @return the coordinates following the y axis
     */
    double ConvertToY(double lat, double lon) {
        return 111.0 * lat;
    }


    /**
     * @param intersection location the includes a latitude and longitude
     * @return where to place it on the screen along the x axis
     */
    double positionX(Intersection intersection) {
        return this.ratioWidth()
                * (0.05 + 0.90 * ((this.ConvertToX(intersection.getLatitude(), intersection.getLongitude()) - this.minX)
                        / (this.maxX - this.minX)))
                + this.offsetX;
    }
    /**
     * @param intersection location the includes a latitude and longitude
     * @return where to place it on the screen along the y axis
     */
    double positionY(Intersection intersection) {
        return this.ratioHeight()
                * (0.05 + 0.90 * ((this.ConvertToY(intersection.getLatitude(), intersection.getLongitude()) - this.minY)
                        / (this.maxY - this.minY)))
                + this.offsetY;

    }
    /**
     * @return the maximum height the map can fit in without stretching or cutting parts of the map off in width
     */
    double ratioHeight() {
        return Math.min(this.getHeight(), this.getWidth() * (this.maxY - this.minY) / (this.maxX - this.minX));
    }
    /**
     * @return the maximum width the map can fit in without stretching or cutting parts of the map off in height
     */
    double ratioWidth() {
        return Math.min(this.getWidth(), this.getHeight() * (this.maxX - this.minX) / (this.maxY - this.minY));
    }
    /**
     * zooms in or out based on the direction of scrolling and moves the screen to stay centered around the position of the mouse
     * @param event a scroll event
     */
    private void handleScroll(ScrollEvent event){
        double delta = event.getDeltaY(); // Positive for zoom in, negative for zoom out
        double scaleFactor = 1.05; // Adjust the zoom factor as needed
        if (delta > 0) {
            // Zoom in
            this.offsetX=this.offsetX-this.mouseX*(scaleFactor-1)/this.zoomTransform.getX();
            this.offsetY=this.offsetY-this.mouseY*(scaleFactor-1)/this.zoomTransform.getX();
            this.zoomTransform.setX(this.zoomTransform.getX() * scaleFactor);
            this.zoomTransform.setY(this.zoomTransform.getY() * scaleFactor);

        } else if (delta < 0) {
            // Zoom out
            this.offsetX=this.offsetX+this.mouseX*((1-(1/scaleFactor)))/this.zoomTransform.getX();
            this.offsetY=this.offsetY+this.mouseY*((1-(1/scaleFactor)))/this.zoomTransform.getX();
            this.zoomTransform.setX(this.zoomTransform.getX() / scaleFactor);
            this.zoomTransform.setY(this.zoomTransform.getY() / scaleFactor);
        }
        this.resizeButton();
        this.draw();

    }
    /**
     * Saves the location of a mousepress
     * @param event information from when a mouse is pressed
     */
    // Action to make the dragging possible
    private void handleMousePressed(MouseEvent event) {
        this.initialX = event.getSceneX();
        this.initialY = event.getSceneY();
    }
    /**
     * Shifts the screen to follow the dragging
     * @param event information includinglocation of the mouse at the point in the drag
     */
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
