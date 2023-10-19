package fr.blazanome.routeplanner.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {
    @Override
    public void start(Stage stage) {

        HBox mainPane = new HBox();
        VBox leftPane = new VBox();




        //Creating a Scene

        //Setting title to the scene

        //Adding the scene to the stage
        //stage.setScene(scene);

        //Displaying the contents of a scene



        HBox controlsBox = new HBox();
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");
        controlsBox.getChildren().addAll(undoButton, redoButton, loadButton, saveButton);
        controlsBox.setSpacing(10);

        leftPane.getChildren().addAll(controlsBox);
        Insets insets = new Insets(10, 10, 10, 10);
        for (Node child : leftPane.getChildren()) {
            VBox.setMargin(child, insets);
        }
        Scene scene = new Scene(mainPane);
        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();
        Map m=new Map();
        m.setStyle("-fx-background-color: green;");
        m.setMinSize(500, 500);
        m.setMaxSize(700, 700);
        mainPane.getChildren().addAll(leftPane, m);
        m.drawMap();
    }
    public static void main(String[] args) {
        Window.launch();
    }

}