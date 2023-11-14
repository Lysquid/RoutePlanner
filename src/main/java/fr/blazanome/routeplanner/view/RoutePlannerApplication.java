package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RoutePlannerApplication extends Application {

    protected Controller controller;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/window.fxml"));
        Parent root = loader.load();
        this.controller = loader.<GraphicalView>getController().getController();
        Scene scene = new Scene(root);

        stage.setTitle("Route Planner");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        this.controller.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}
