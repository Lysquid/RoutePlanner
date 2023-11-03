package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.observer.Observable;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

public class GraphicalView implements View {

    protected final Controller controller;

    public Text countCourier;
    public MapView mapView;
    public ComboBox<String> deliveryCourier;
    public ComboBox<String> deliveryTime;
    public Label deliveryIntersection;

    public Button addDelivery;

    public GraphicalView() {
        this.controller = new Controller(this);
    }

    public void loadMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        controller.loadMap(file);
    }

    public void undo(ActionEvent actionEvent) {
        this.controller.undo();
    }

    public void redo(ActionEvent actionEvent) {
        this.controller.redo();
    }

    public void addDelivery(ActionEvent actionEvent) {
        this.controller.addDelivery();
    }

    public void compute(ActionEvent actionEvent) {
        this.controller.compute();
    }

    public void selectIntersection(ActionEvent actionEvent) {
        ButtonIntersection button = (ButtonIntersection) actionEvent.getSource();
        this.controller.selectIntersection(button.getIntersection());
    }

    public void setDisableAddDelivery(boolean bool) {
        this.addDelivery.setDisable(bool);
    }

    @Override
    public void update(Observable observable, Object message) {
        if (message instanceof IMap) {
            this.mapView.setUp((Session) observable);
            this.mapView.draw((Session) observable);
        } else if (message instanceof Session) {
            this.mapView.draw((Session) observable);
        }
    }
}
