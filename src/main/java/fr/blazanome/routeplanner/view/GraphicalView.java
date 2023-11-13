package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.DeliverySelectedState;
import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Delivery;
import fr.blazanome.routeplanner.model.DeliveryRequest;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.model.Timeframe;
import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;
import fr.blazanome.routeplanner.observer.Observer;
import fr.blazanome.routeplanner.observer.Observers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
    public ComboBox<Timeframe> timeframe;
    public Button addDelivery;
    public Button removeDelivery;
    public TableView<DeliveryRequest> deliveriesTable;
    public TableView<Delivery> planningTable;

    public Button undoButton;
    public Button redoButton;
    public boolean oneCourier;

    public java.util.Map<Button, List<Class<? extends State>>> buttonEnabledStates = new HashMap<>();
    private Observer commandStackObserver;

    public GraphicalView() {
        this.commandStackObserver = Observers.typed(CommandStack.class, this::onCommandStackUpdate);
        this.controller = new Controller(this);
        this.controller.getCommandStack().addObserver(this.commandStackObserver);
        this.oneCourier = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.timeframe.setItems(FXCollections.observableArrayList(Timeframe.values()));
        this.timeframe.setValue(Timeframe.H8);
        this.deliveriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.planningTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.buttonEnabledStates.put(this.addDelivery, (Arrays.asList(IntersectionSelectedState.class)));
        this.buttonEnabledStates.put(this.removeDelivery, (Arrays.asList(DeliverySelectedState.class)));

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
        this.controller.loadMap(file);
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

    public void setDisableAddDelivery(boolean bool) {
        this.addDelivery.setDisable(bool);
    }

    @Override
    public void onStateChange(Controller controller, State state) {
        for (var entry : this.buttonEnabledStates.entrySet()) {
            if (entry.getValue().contains(state.getClass())) {
                entry.getKey().setDisable(false);
            } else {
                entry.getKey().setDisable(true);
            }
        }
    }

    @Override
    public void update(Observable observable, EventType eventType, Object message) {
        switch (eventType) {
            case MAP_LOADED -> {
                Session session = (Session) observable;
                this.mapView.setUp(session);
                if (oneCourier) {
                    this.mapView.draw(this.selectedCourier.getValue());
                } else {
                    this.mapView.draw(null);
                }
                this.updateCouriers(session);
                this.selectedCourier.setValue(session.getCouriers().get(0));
            }
            case ROUTE_COMPUTED -> {

                if (oneCourier) {
                    this.mapView.draw(this.selectedCourier.getValue());
                } else {
                    this.mapView.draw(null);
                }

                if (message == null) {
                    this.showRouteComputeError((Courier) observable);
                }

                this.updateRequests();
            }
            case COURIER_REMOVE -> {
                if (oneCourier) {
                    this.mapView.draw(this.selectedCourier.getValue());
                } else {
                    this.mapView.draw(null);
                }
                this.updateCouriers((Session) observable);
            }
            case COURIER_ADD -> {
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
        }
    }

    public void drawOnlySelectCourier() {
        this.oneCourier = !this.oneCourier;
        if (oneCourier) {
            this.mapView.draw(this.selectedCourier.getValue());
        }
    }

    private void updateCouriers(Session session) {
        this.selectedCourier.setItems(FXCollections.observableArrayList(session.getCouriers()));
        this.selectedCourier.setValue(session.getCouriers().get(session.getCouriers().size() - 1));
    }

    public void selectCourier(ActionEvent actionEvent) {
        this.updateRequests();
        if (this.oneCourier) {
            this.mapView.draw(this.selectedCourier.getValue());
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
        this.controller.loadSession(file);
    }

    public void saveSession(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the session");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showSaveDialog(button.getScene().getWindow());
        this.controller.saveSession(file);
    }
}
