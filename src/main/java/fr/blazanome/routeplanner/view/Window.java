package fr.blazanome.routeplanner.view;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Window extends Application {

    @Override
    public void start(Stage stage) {

        HBox mainPane = new HBox();
        VBox leftPane = new VBox();
        StackPane mapPane = new StackPane();

        mapPane.setStyle("-fx-background-color: green;");
        Label lab1 = new Label("Map");
        mapPane.getChildren().add(lab1);
        mapPane.setMinSize(700, 700);

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

        mainPane.getChildren().addAll(leftPane, mapPane);

        Scene scene = new Scene(mainPane);
        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Window.launch();
    }

}