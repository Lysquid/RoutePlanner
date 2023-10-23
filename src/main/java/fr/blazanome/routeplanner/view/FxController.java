package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.observer.Observable;
import fr.blazanome.routeplanner.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class FxController implements Observer {

    protected final Controller controller;

    public Text countCourier;
    public Map map;
    public ComboBox<String> deliveryCourier;
    public ComboBox<String> deliveryTime;
    public Label deliveryIntersection;

    public Button addDelivery;

    public FxController() {
        this.controller = new Controller();
        this.controller.addObserver(this);
    }

    public void loadMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        controller.loadMap(file);
    }

    public void loadHardCodedMap(ActionEvent actionEvent) {
        this.map.draw();
    }

    public void undo(ActionEvent actionEvent) {
        this.controller.undo();
    }

    public void redo(ActionEvent actionEvent) {
        this.controller.redo();
    }


    @Override
    public void update(Observable observable, Object message) {
        this.addDelivery.setDisable(!(message instanceof IntersectionSelectedState));
    }
}
