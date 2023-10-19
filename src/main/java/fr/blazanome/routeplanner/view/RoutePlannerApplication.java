package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RoutePlannerApplication extends Application {

    protected Controller controller;

    public Text countCourier;
    public Map map;
    protected Stage mainStage;


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/window.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}