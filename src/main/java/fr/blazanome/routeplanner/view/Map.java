package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.IHMTestMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.HashMap;


public class Map extends Pane{
    private Group root = new Group();
    private Button active = null;
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxY = 0;
    double maxX = 0;

    GraphicsContext gc;
    Canvas canvas;
    IHMTestMap data = new IHMTestMap();

    ArrayList<Button> buttonIntersectionList=new ArrayList<>();


    HashMap<Button,Intersection> buttonIntersection= new HashMap<>();

    Scale zoomTransform = new Scale(1.0, 1.0); // Initial scale is 1.0

    public Map() {
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // Add scale transformation to the root
        root.getTransforms().add(zoomTransform);

        // Handle mouse scroll events for zooming
        setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
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

                // Redraw the map
                redraw();
            }
        });

        widthProperty().addListener((observable, oldValue, newValue) -> redraw());
        heightProperty().addListener((observable, oldValue, newValue) -> redraw());
    }
    void draw(){
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        Iterable<Intersection> iterableIntersection=data.getIntersections();
        for (Intersection intersection : iterableIntersection) {
            minX=Math.min(minX,ConvertToX(intersection.getLatitude(),intersection.getLongitude()));
            minY=Math.min(minY,ConvertToY(intersection.getLatitude(),intersection.getLongitude()));
            maxX=Math.max(maxX,ConvertToX(intersection.getLatitude(),intersection.getLongitude()));
            maxY=Math.max(maxY,ConvertToY(intersection.getLatitude(),intersection.getLongitude()));
        }
        this.drawPlainSegments(data.getSegments());
        this.drawPlainIntersections(iterableIntersection);
        Intersection warehouseLocation=data.getWarehouse();
        Rectangle warehouseItem= new Rectangle(8,8);
        warehouseItem.layoutXProperty().bind(widthProperty().multiply(0.05+0.90*((ConvertToX(warehouseLocation.getLatitude(),warehouseLocation.getLongitude())-minX)/(maxX-minX))));
        warehouseItem.layoutYProperty().bind(heightProperty().multiply(0.05+0.90*((ConvertToY(warehouseLocation.getLatitude(),warehouseLocation.getLongitude())-minY)/(maxY-minY))));
        root.getChildren().add(warehouseItem);
        this.getChildren().add(root);

    }
    void redraw(){
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        // Re-draw everything on the canvas
        gc.clearRect(0, 0, getWidth(), getHeight());
        this.drawPlainSegments(data.getSegments());
        this.updateIntersections();

    }
    void drawPlainSegments(Iterable<Segment> iterableSegment) {
        for (Segment segment : iterableSegment) {
            gc.setStroke(Color.BLUE);
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


    void drawPlainIntersections(Iterable<Intersection> iterableIntersection){
        EventHandler<ActionEvent> event = e -> {
            Button clicked=(Button)e.getSource();
            clicked.setStyle("-fx-background-color: #ff0000; ");
            if(active!=null){
                active.setStyle("-fx-background-color: #ffffff; ");
            }
            active=clicked;
            Intersection selectedintersection =buttonIntersection.get(clicked);
            System.out.println(selectedintersection.getLatitude()+" ; "+selectedintersection.getLongitude());

        };
        int radius=5;
        Circle c= new Circle(radius);
        for (Intersection intersection : iterableIntersection) {
            Button bt = new Button();
            bt.setShape(c);
            bt.setMaxSize(2*radius,2*radius);
            bt.setMinSize(2*radius,2*radius);
            bt.setLayoutX(positionX(intersection)-radius);
            bt.setLayoutY(positionY(intersection)-radius);
            bt.setOnAction(event);
            root.getChildren().add(bt);
            buttonIntersection.put(bt,intersection);
            buttonIntersectionList.add(bt);
        }

    }
    void updateIntersections(){
        int radius=5;
        for (Button bt : buttonIntersectionList) {
            Intersection intersection =buttonIntersection.get(bt);
            bt.setLayoutX(positionX(intersection)-radius);
            bt.setLayoutY(positionY(intersection)-radius);
        }

    }
    double ConvertToX(double lat, double lon){
        return Math.cos((Math.PI/180.0)*lat)*111.0*lon;
    }
    double ConvertToY(double lat, double lon){
        return 111.0*lat;
    }
    double positionX(Intersection intersection){
        return this.getWidth()*(0.05+0.90*((ConvertToX(intersection.getLatitude(),intersection.getLongitude())-minX)/(maxX-minX)));
    }
    double positionY(Intersection intersection){
        return this.getHeight()*(0.05+0.90*((ConvertToY(intersection.getLatitude(),intersection.getLongitude())-minY)/(maxY-minY)));

    }
}

