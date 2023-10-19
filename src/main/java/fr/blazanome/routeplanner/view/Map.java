package fr.blazanome.routeplanner.view;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
public class Map extends Pane{
    void draw(){

        Group root = new Group();
        for (int i=0;i<40;i++) {
            //Setting the properties to a line
            Line line = new Line();
            line.startXProperty().bind(heightProperty().divide(2));
            line.setStartY(10*i);
            line.setEndX(10*i);
            line.setEndY(10*i);
            root.getChildren().add(line);                //Creating a Group
        }
        Line line = new Line();

        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(0);
        root.getChildren().add(line);
        Button bt = new Button();
        Circle c= new Circle(5);
        bt.setShape(c);
        bt.setMaxSize(10,10);
        bt.setMinSize(10,10);
        bt.setLayoutX(10);
        bt.setLayoutY(10);
        bt.layoutXProperty().bind(widthProperty().divide(2));
        bt.layoutYProperty().bind(heightProperty().divide(2));

        root.getChildren().add(bt);                //Creating a Group
        this.getChildren().add(root);

    }
}
