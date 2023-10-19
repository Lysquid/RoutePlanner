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

public class Window extends Application {

    protected Controller controller;

    public Text countCourier;
    public Map map;
    protected Stage mainStage;


    @Override
    public void start(Stage stage) throws IOException {

        controller = new Controller();
        mainStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/window.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    @FXML
    protected void handleUndo(ActionEvent actionEvent) {
        this.controller.undo(this.controller);
    }

    public void handleRedo(ActionEvent actionEvent) {
        this.controller.redo(this.controller);
    }

    @FXML
    protected void handleLoadMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("XML file", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            controller.loadMap(selectedFile);
        }
    }

    @FXML
    protected void handleLoadMapHardCoded(ActionEvent actionEvent) {
        this.map.drawMap();
    }
}