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

import java.util.HashMap;


public class Map extends Pane{
    private Group root;
    private Button active=null;
    double minX=Double.MAX_VALUE;
    double minY=Double.MAX_VALUE;
    double maxY=0;
    double maxX=0;

    HashMap<Button,Intersection> buttonIntersection= new HashMap<>();
    void draw(){
        IHMTestMap data=new IHMTestMap();
        root = new Group();
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
    void drawPlainSegments(Iterable<Segment> iterableSegment){
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Segment segment : iterableSegment) {
            //We will need to add
            Line line = new Line();


            //The use of lines is temporary
            Intersection start=segment.getOrigin();
            line.startXProperty().bind(widthProperty().multiply(0.05+0.90*((ConvertToX(start.getLatitude(),start.getLongitude())-minX)/(maxX-minX))));
            line.startYProperty().bind(heightProperty().multiply(0.05+0.90*((ConvertToY(start.getLatitude(),start.getLongitude())-minY)/(maxY-minY))));
            Intersection end=segment.getDestination();
            line.endXProperty().bind(widthProperty().multiply(0.05+0.90*((ConvertToX(end.getLatitude(),end.getLongitude())-minX)/(maxX-minX))));
            line.endYProperty().bind(heightProperty().multiply(0.05+0.90*((ConvertToY(end.getLatitude(),end.getLongitude())-minY)/(maxY-minY))));
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
            bt.layoutXProperty().bind(widthProperty().multiply(0.05+0.90*((ConvertToX(intersection.getLatitude(),intersection.getLongitude())-minX)/(maxX-minX))).subtract(radius));
            bt.layoutYProperty().bind(heightProperty().multiply(0.05+0.90*((ConvertToY(intersection.getLatitude(),intersection.getLongitude())-minY)/(maxY-minY))).subtract(radius));
            bt.setOnAction(event);
            root.getChildren().add(bt);
            buttonIntersection.put(bt,intersection);

        }

    }
    double ConvertToX(double lat, double lon){
        return Math.cos((Math.PI/180.0)*lat)*111.0*lon;
    }
    double ConvertToY(double lat, double lon){
        return 111.0*lat;
    }
}

