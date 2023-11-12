package fr.blazanome.routeplanner.view;

import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.Controller;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GraphicalView implements View, Initializable {

    protected final Controller controller;

    public MapView mapView;
    public ComboBox<Courier> selectedCourier;
    public ComboBox<Timeframe> timeframe;
    public Button addDelivery;
    public TableView<DeliveryRequest> deliveriesTable;
    public TableView<Delivery> planningTable;

    public Button undoButton;
    public Button redoButton;

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

    public void selectIntersection(ActionEvent actionEvent) {
        ButtonIntersection button = (ButtonIntersection) actionEvent.getSource();
        this.controller.selectIntersection(button.getIntersection());
    }

    public void setDisableAddDelivery(boolean bool) {
        this.addDelivery.setDisable(bool);
    }

    @Override
    public void update(Observable observable, EventType eventType, Object message) {
        if (message instanceof IMap) {
            Session session = (Session) observable;
            this.mapView.setUp(session);
            this.mapView.draw();
            this.updateCouriers(session);
            this.selectedCourier.setValue(session.getCouriers().get(0));
        } else if (eventType == EventType.ROUTE_COMPUTED) {
            this.mapView.draw();
            this.updateRequests();
        } else if (observable instanceof Courier courier) {
            this.updateRequests();
        } else if (observable instanceof Session session) {
            this.updateCouriers(session);
        }
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

    private void updateCouriers(Session session) {
        this.selectedCourier.setItems(FXCollections.observableArrayList(session.getCouriers()));
        this.selectedCourier.setValue(session.getCouriers().get(session.getCouriers().size()-1));
    }

    public void selectCourier(ActionEvent actionEvent) {
        this.updateRequests();
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
