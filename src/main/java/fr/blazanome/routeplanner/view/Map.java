package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.model.IHMTestMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Map extends Pane{
    private Group root;
    private Button active=null;
    double minLat=Double.MAX_VALUE;
    double minLong=Double.MAX_VALUE;
    double maxLat=0;
    double maxLong=0;
    void draw(){
        IHMTestMap data=new IHMTestMap();
        root = new Group();
        Iterable<Intersection> iterableIntersection=data.getIntersections();
        for (Intersection intersection : iterableIntersection) {
            minLat=Math.min(minLat,intersection.getLatitude());
            minLong=Math.min(minLong,intersection.getLongitude());
            maxLat=Math.max(maxLat,intersection.getLatitude());
            maxLong=Math.max(maxLong,intersection.getLongitude());
        }
        this.drawPlainSegments(data.getSegments());
        this.drawPlainIntersections(iterableIntersection);
        Intersection warehouseLocation=data.getWarehouse();
        Rectangle warehouseItem= new Rectangle(8,8);
        warehouseItem.layoutXProperty().bind(widthProperty().multiply(0.05+0.90*(warehouseLocation.getLatitude()-minLat)/(maxLat-minLat)));
        warehouseItem.layoutYProperty().bind(heightProperty().multiply(0.05+0.90*(warehouseLocation.getLongitude()-minLong)/(maxLong-minLong)));
        root.getChildren().add(warehouseItem);
        this.getChildren().add(root);

    }
    void drawPlainSegments(Iterable<Segment> iterableSegment){
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Segment segment : iterableSegment) {
            //We will need to add
            Line line = new Line();


            //The use of lines is temporary
            line.startXProperty().bind(widthProperty().multiply(0.05+0.90*(segment.getOrigin().getLatitude()-minLat)/(maxLat-minLat)));
            line.startYProperty().bind(heightProperty().multiply(0.05+0.90*(segment.getOrigin().getLongitude()-minLong)/(maxLong-minLong)));
            line.endXProperty().bind(widthProperty().multiply(0.05+0.90*(segment.getDestination().getLatitude()-minLat)/(maxLat-minLat)));
            line.endYProperty().bind(heightProperty().multiply(0.05+0.90*(segment.getDestination().getLongitude()-minLong)/(maxLong-minLong)));
            //gc.strokeLine(canvas.getHeight()*(0.05+0.90*(segment.getOrigin().getLatitude()-minLat)/(maxLat-minLat)), canvas.getHeight()*(0.05+0.90*(segment.getOrigin().getLongitude()-minLong)/(maxLong-minLong)),canvas.getHeight()*(0.05+0.90*(segment.getDestination().getLatitude()-minLat)/(maxLat-minLat)), canvas.getHeight()*(0.05+0.90*(segment.getDestination().getLongitude()-minLong)/(maxLong-minLong)));
            root.getChildren().add(line);
        }
        //root.getChildren().add(gc.getCanvas());
    }
    void drawPlainIntersections(Iterable<Intersection> iterableIntersection){
        EventHandler<ActionEvent> event = e -> {
            Button clicked=(Button)e.getSource();
            clicked.setStyle("-fx-background-color: #ff0000; ");
            if(active!=null){
                active.setStyle("-fx-background-color: #ffffff; ");
            }
            active=clicked;
        };
        int radius=5;
        Circle c= new Circle(radius);
        for (Intersection intersection : iterableIntersection) {
            Button bt = new Button();
            bt.setShape(c);
            bt.setMaxSize(2*radius,2*radius);
            bt.setMinSize(2*radius,2*radius);
            bt.layoutXProperty().bind(widthProperty().multiply(0.05+0.90*((intersection.getLatitude()-minLat)/(maxLat-minLat))).subtract(radius));
            bt.layoutYProperty().bind(heightProperty().multiply(0.05+0.90*((intersection.getLongitude()-minLong)/(maxLong-minLong))).subtract(radius));
            bt.setOnAction(event);
            root.getChildren().add(bt);
        }

    }
}
