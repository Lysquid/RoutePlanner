package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
import fr.blazanome.routeplanner.controller.state.RequestSelectedState;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GraphicalView implements View, Initializable {

    private final Controller controller;

    @FXML
    private MapView mapView;
    @FXML
    private ComboBox<Courier> selectedCourier;
    @FXML
    private CheckBox oneCourier;
    @FXML
    private ComboBox<Timeframe> timeframe;
    @FXML
    private Button addDeliveryButton;
    @FXML
    private Button removeDeliveryButton;
    @FXML
    private TableView<DeliveryRequest> deliveriesTable;
    @FXML
    private TableView<Delivery> planningTable;
    @FXML
    private Text numberRequests;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Text taskStatus;
    @FXML
    private Button removeCourierButton;
    @FXML
    private Button loadSessionButton;
    @FXML
    private Button saveSessionButton;
    @FXML
    private Button addCourierButton;
    @FXML
    private Button resetZoomButton;
    @FXML
    private Button cancelTasksButton;
    @FXML
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

        this.planningTable.setRowFactory(tv -> {
            TableRow<Delivery> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    this.onDeliveryTableSelect(row.getItem().getRequest());
                }
            });

            return row;
        });

        this.onStateChange(this.controller, this.controller.getCurrentState());
    }

    @FXML
    private void loadMap(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.loadMap(file);
        }
    }

    @FXML
    private void undo(ActionEvent actionEvent) {
        this.controller.undo();
    }

    @FXML
    private void redo(ActionEvent actionEvent) {
        this.controller.redo();
    }

    @FXML
    private void addRequest(ActionEvent actionEvent) {
        this.controller.addRequest(this.selectedCourier.getValue(), this.timeframe.getValue());
    }

    @FXML
    private void removeRequest(ActionEvent event) {
        this.controller.removeRequest();
    }

    @FXML
    private void selectIntersection(ActionEvent actionEvent) {
        ButtonIntersection button = (ButtonIntersection) actionEvent.getSource();
        this.controller.selectIntersection(button.getIntersection());
    }

    private void onDeliveryTableSelect(DeliveryRequest request) {
        this.controller.selectRequest(request, this.selectedCourier.getValue());
    }

    @Override
    public void onStateChange(Controller controller, State state) {
        if (state instanceof NoMapState)
            return;
        this.addDeliveryButton.setDisable(!(state instanceof IntersectionSelectedState) || this.selectedCourier.getValue() == null);
        this.removeDeliveryButton.setDisable(!(state instanceof RequestSelectedState));
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
                this.resetZoomButton.setDisable(false);
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
                this.deliveriesTable.getSelectionModel().select((DeliveryRequest) message);
            }
        }
    }

    private void showRouteComputeError(Courier courier) {
        Alert alert = new Alert(AlertType.ERROR, "No route can fulfill your requirements for " + courier.toString() + " or you have canceled the computations too early.");
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
            this.numberRequests.setText(String.valueOf(courier.getRequests().size()));
        } else {
            this.deliveriesTable.setItems(FXCollections.emptyObservableList());
            this.planningTable.setItems(FXCollections.emptyObservableList());
        }
    }

    @FXML
    private void setOnlyOneCourier(){
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

    @FXML
    private void selectCourier(ActionEvent actionEvent) {
        this.updateRequests();
        if (this.oneCourier.isSelected()) {
            this.mapView.setSelectedCourier(this.selectedCourier.getValue());
        }

        this.controller.selectCourier(this.selectedCourier.getValue());
    }

    @FXML
    private void addCourier(ActionEvent actionEvent) {
        this.controller.addCourier();
    }

    @FXML
    private void removeCourier(ActionEvent actionEvent) {
        this.controller.removeCourier(this.selectedCourier.getValue());
    }

    @FXML
    private void loadSession(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a session");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.loadSession(file);
        }
    }

    @FXML
    private void saveSession(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the session");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML file", "*.xml"));
        Button button = (Button) actionEvent.getSource();
        File file = fileChooser.showSaveDialog(button.getScene().getWindow());
        if (file != null) {
            this.controller.saveSession(file);
        }
    }

    @FXML
    private void resetZoom(){
        this.mapView.resetPosition();
    }

    @Override
    public void onTaskCountChange(int taskCount) {
        if (taskCount == 1) {
            this.taskStatus.setText("Computing route for " + taskCount + " courier...");
            this.cancelTasksButton.setDisable(false);
        } else if (taskCount > 1) {
            this.taskStatus.setText("Computing route for " + taskCount + " couriers...");
            this.cancelTasksButton.setDisable(false);
        } else {
            this.taskStatus.setText("No task running");
            this.cancelTasksButton.setDisable(true);
        }
    }

    @FXML
    private void cancelTasks() {
        this.controller.cancelTasks();
    }

    public Controller getController() {
        return controller;
    }
}
