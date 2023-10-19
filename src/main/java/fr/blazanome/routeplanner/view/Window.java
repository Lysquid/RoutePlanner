package fr.blazanome.routeplanner.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Window extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        BorderPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/window.fxml")));

        Scene scene = new Scene(root);

        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();

        Map m=new Map();
        m.setStyle("-fx-background-color: green;");
        m.setMinSize(500, 500);
        m.setMaxSize(700, 700);
        root.setCenter(m);
        m.drawMap();
    }
    public static void main(String[] args) {
        Window.launch();
    }

}