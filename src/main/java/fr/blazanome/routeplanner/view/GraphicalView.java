package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.DeliverySelectedState;
import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Delivery;
import fr.blazanome.routeplanner.model.DeliveryRequest;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.model.Timeframe;
import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;
import fr.blazanome.routeplanner.observer.Observer;
import fr.blazanome.routeplanner.observer.Observers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class GraphicalView implements View, Initializable {

    protected final Controller controller;

    public MapView mapView;
    public ComboBox<Courier> selectedCourier;
    public CheckBox oneCourier;
    public ComboBox<Timeframe> timeframe;
    public Button addDeliveryButton;
    public Button removeDeliveryButton;
    public TableView<DeliveryRequest> deliveriesTable;
    public TableView<Delivery> planningTable;
    public Button undoButton;
    public Button redoButton;

    public Button removeCourierButton;
    public Button loadSessionButton;
    public Button saveSessionButton;
    public Button addCourierButton;
    private Observer commandStackObserver;

    public GraphicalView() {
        this.commandStackObserver = Observers.typed(CommandStack.class, this::onCommandStackUpdate);
        this.controller = new Controller(this);
        this.controller.getCommandStack().addObserver(this.commandStackObserver);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.timeframe.setItems(FXCollections.observableArrayList(Timeframe.values()));
        this.timeframe.setValue(Timeframe.H8);
        this.deliveriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.planningTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.deliveriesTable.setRowFactory(tv -> {
            TableRow<DeliveryRequest> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    this.onDeliveryTableSelect(row.getItem());
                }
            });

            return row;
        });

        this.onStateChange(this.controller, this.controller.getCurrentState());
    }

    public void loadMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.loadMap(file);
        }
    }

    public void undo(ActionEvent actionEvent) {
        this.controller.undo();
    }

    public void redo(ActionEvent actionEvent) {
        this.controller.redo();
    }

    public void addDelivery(ActionEvent actionEvent) {
        this.controller.addDelivery(this.selectedCourier.getValue(), this.timeframe.getValue());
    }

    public void removeDelivery(ActionEvent event) {
        this.controller.removeDelivery();
    }

    public void selectIntersection(ActionEvent actionEvent) {
        ButtonIntersection button = (ButtonIntersection) actionEvent.getSource();
        this.controller.selectIntersection(button.getIntersection());
    }

    public void onDeliveryTableSelect(DeliveryRequest request) {
        this.controller.selectDelivery(request, this.selectedCourier.getValue());
    }

    @Override
    public void onStateChange(Controller controller, State state) {
        if (state instanceof NoMapState)
            return;
        this.addDeliveryButton.setDisable(!(state instanceof IntersectionSelectedState) || this.selectedCourier.getValue() == null);
        this.removeDeliveryButton.setDisable(!(state instanceof DeliverySelectedState));
        this.mapView.onStateChange(controller, state);
    }

    @Override
    public void update(Observable observable, EventType eventType, Object message) {
        switch (eventType) {
            case MAP_LOADED -> {
                Session session = (Session) observable;
                this.mapView.setUp(session);
                this.mapView.draw();

                this.updateCouriers(session);
                this.selectedCourier.setValue(session.getCouriers().get(0));

                this.loadSessionButton.setDisable(false);
                this.saveSessionButton.setDisable(false);
                this.addCourierButton.setDisable(false);
                this.removeCourierButton.setDisable(false);
            }
            case ROUTE_COMPUTED -> {

                Courier courier = (Courier) observable;
                this.mapView.draw();
                if (message == null && !courier.getRequests().isEmpty()) {
                    this.showRouteComputeError((Courier) observable);
                }

                this.updateRequests();
            }
            case COURIER_REMOVE, COURIER_ADD -> {
                this.mapView.draw();
                this.updateCouriers((Session) observable);
            }
            case DELIVERY_ADD, DELIVERY_REMOVE -> {
                this.updateRequests();
            }
        }
    }

    private void showRouteComputeError(Courier courier) {
        Alert alert = new Alert(AlertType.ERROR, "No route can fulfill your requirements for " + courier.toString());
        alert.showAndWait();
    }

    private void onCommandStackUpdate(CommandStack commandStack, EventType eventType, Object message) {
        if (Objects.requireNonNull(eventType) == EventType.COMMAND_STACK_UPDATE) {
            this.undoButton.setDisable(!commandStack.canUndo());
            this.redoButton.setDisable(!commandStack.canRedo());
        }
    }

    private void updateRequests() {
        Courier courier = this.selectedCourier.getValue();
        if (courier != null) {
            this.deliveriesTable.setItems(FXCollections.observableArrayList(courier.getRequests()));
            if (courier.getRoute() != null) {
                this.planningTable.setItems(FXCollections.observableArrayList(courier.getRoute().getPlanning()));
            } else {
                this.planningTable.setItems(FXCollections.emptyObservableList());
            }
        } else {
            this.deliveriesTable.setItems(FXCollections.emptyObservableList());
            this.planningTable.setItems(FXCollections.emptyObservableList());
        }
    }

    public void setOnlyOneCourier(){
        if (this.oneCourier.isSelected()) {
            this.mapView.setSelectedCourier(this.selectedCourier.getValue());
        } else{
            this.mapView.setSelectedCourier(null);
        }
    }

    private void updateCouriers(Session session) {
        this.selectedCourier.setItems(FXCollections.observableArrayList(session.getCouriers()));
        if (!session.getCouriers().isEmpty())
            this.selectedCourier.setValue(session.getCouriers().get(session.getCouriers().size() - 1));
        this.removeCourierButton.setDisable(session.getCouriers().isEmpty());
        this.addDeliveryButton.setDisable(this.addDeliveryButton.isDisabled() && this.selectedCourier.getValue() == null);
    }

    public void selectCourier(ActionEvent actionEvent) {
        this.updateRequests();
        if (this.oneCourier.isSelected()) {
            this.mapView.setSelectedCourier(this.selectedCourier.getValue());
        }

        this.controller.selectCourier(this.selectedCourier.getValue());
    }

    public void addCourier(ActionEvent actionEvent) {
        this.controller.addCourier();
    }

    public void removeCourier(ActionEvent actionEvent) {
        this.controller.removeCourier(this.selectedCourier.getValue());
    }

    public void loadSession(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a session");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.loadSession(file);
        }
    }

    public void saveSession(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the session");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showSaveDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.saveSession(file);
        }
    }
}
