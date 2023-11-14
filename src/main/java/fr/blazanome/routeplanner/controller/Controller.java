package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.controller.state.MapLoadedState;
import fr.blazanome.routeplanner.algorithm.AnytimeCourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.CourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observer;
import fr.blazanome.routeplanner.observer.Observers;
import fr.blazanome.routeplanner.tools.XMLMapParser;
import fr.blazanome.routeplanner.view.View;

import java.io.File;

/**
 * Controller of the MVC architecture, following a State pattern
 */
public class Controller {

    private final View view;
    private Session session;
    private State currentState;
    private final CommandStack commandStack;
    private final Observer sessionObserver;
    private final Observer courierObserver;
    private final CourierRouteUpdater courierRouteUpdater;

    /**
     * @param view  The view of the MVC architecture
     */
    public Controller(View view) {
        this.view = view;
        this.setCurrentState(new NoMapState());
        this.commandStack = new CommandStack();

        var tourGenerationAlgorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1.Factory());
        this.courierRouteUpdater = new AnytimeCourierRouteUpdater(this.view::onTaskCountChange, tourGenerationAlgorithm);

        this.sessionObserver = Observers.typed(Session.class, this::onSessionChange);
        this.courierObserver = Observers.typed(Courier.class, this::onCourierChange);
    }

    /**
     * Set the new state of the state pattern
     */
    public void setCurrentState(State newState) {
        this.currentState = newState;
        this.view.onStateChange(this, newState);
    }

    /**
     * Load a map from a file
     */
    public void loadMap(File file) {
        this.currentState.loadMap(this, file);
    }

    /**
     * Undo the last action, using the command pattern
     */
    public void undo() {
        this.currentState.undo(this.commandStack);
    }

    /**
     * Redo the last undone action, using the command pattern
     */
    public void redo() {
        this.currentState.redo(this.commandStack);
    }

    /**
     * Compute a route for a courier
     */
    public void compute(Courier courier) {
        this.currentState.compute(courier, this.courierRouteUpdater, this.session);
    }

    /**
     * Called when an intersection was clicked on the view
     */
    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    /**
     * Add a delivery request to a courier
     * @param courier   the courier who must do the request
     * @param timeframe the chosen timeframe
     */
    public void addRequest(Courier courier, Timeframe timeframe) {
        this.currentState.addRequest(this, courier, timeframe, this.commandStack);
    }

    /**
     * Called when a request is selected in the view
     * @param courier   the courier to whom the request belong
     */
    public void selectRequest(DeliveryRequest deliveryRequest, Courier courier) {
        this.currentState.selectRequest(this, deliveryRequest, courier);
    }

    /**
     * Remove a request
     */
    public void removeRequest() {
        this.currentState.removeRequest(this, this.commandStack);
    }

    /**
     * @return  the command stack of the command pattern
     */
    public CommandStack getCommandStack() {
        return commandStack;
    }

    /**
     * @return  the current state
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * @param session   the session of the controller, encapsulating the model
     */
    public void setSession(Session session) {
        if (this.session != null) {
            this.session.removeObserver(this.sessionObserver);
            this.session.removeObserver(this.view);
        }
        this.session = session;
        this.session.addObserver(this.sessionObserver);
        this.session.addObserver(this.view);
    }

    /**
     * Add a new courier
     */
    public void addCourier() {
        this.currentState.addCourier(this.session, this.commandStack);
    }

    /**
     * Remove an existing courier
     */
    public void removeCourier(Courier courier) {
        this.currentState.removeCourier(this.session, courier, this.commandStack);
    }

    /**
     * Called when a courier is selected in the view
     */
    public void selectCourier(Courier courier) {
        this.currentState.selectCourier(this, courier);
    }

    /**
     * @return  the map parser for this controller
     */
    public XMLMapParser getMapParser() {
        return new XMLMapParser(new AdjacencyListMap.BuilderFactory());
    }

    /**
     * Observer function of the Session
     * @param eventType an enum describing the change
     * @param message   the object that actually changed
     */
    private void onSessionChange(Session session, EventType eventType, Object message) {
        if (eventType == null) {
            System.err.println("Null event found");
            return;
        }
        switch (eventType) {
            case COURIER_ADD -> {

                Courier courier = (Courier) message;
                courier.addObserver(this.courierObserver);
                courier.addObserver(this.view);
            }
            case COURIER_REMOVE -> {
                Courier courier = (Courier) message;
                courier.removeObserver(this.courierObserver);
                courier.removeObserver(this.view);
            }
            default -> {
            }
        }
    }

    /**
     * Observer function of the couriers
     * @param eventType an enum describing the change
     * @param message   the object that actually changed
     */
    private void onCourierChange(Courier courier, EventType eventType, Object message) {
        if (eventType == null) {
            System.err.println("Null event found");
            return;
        }
        switch (eventType) {
            case DELIVERY_ADD, DELIVERY_REMOVE -> {
                this.compute(courier);
            }
            default -> {
            }
        }
    }

    /**
     * Save the current session to a file
     */
    public void saveSession(File file) {
        this.currentState.saveSession(file, this.session);
    }

    /**
     * Load a session from a file
     * The map must already be loaded
     */
    public void loadSession(File file) {
        this.currentState.loadSession(this, file, this.session, this.view);
        if (session != null) {
            this.setCurrentState(new MapLoadedState());
        }
    }

    /**
     * Cancel the background tasks of computing a route
     */
    public void cancelTasks() {
        this.courierRouteUpdater.cancel();
    }

    /**
     * Cleanup any thread running to gracefully shutdown
     */
    public void shutdown() {
        this.courierRouteUpdater.shutdown();
    }
}
