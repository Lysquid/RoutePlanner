package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.controller.state.MapLoadedState;
import fr.blazanome.routeplanner.algorithm.AnytimeCourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.CourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.SimpleCourierRouteUpdater;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
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

public class Controller {

    private final View view;
    private Session session;
    private State currentState;
    private CommandStack commandStack;

    private Observer sessionObserver;
    private Observer courierObserver;

    private final CourierRouteUpdater courierRouteUpdater;

    private final XMLMapParser mapParser;

    public Controller(View view) {
        this.view = view;
        this.setCurrentState(new NoMapState());
        this.commandStack = new CommandStack();

        this.mapParser = new XMLMapParser(new AdjacencyListMap.BuilderFactory());
        var tourGenerationAlgorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1.Factory());
        this.courierRouteUpdater = new AnytimeCourierRouteUpdater(this.view::onTaskCountChange, tourGenerationAlgorithm);

        this.sessionObserver = Observers.typed(Session.class, this::onSessionChange);
        this.courierObserver = Observers.typed(Courier.class, this::onCourierChange);
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
        this.view.onStateChange(this, newState);
    }

    public void loadMap(File file) {
        this.currentState.loadMap(this, this.view, file);
    }

    public void undo() {
        this.currentState.undo(this.commandStack);
    }

    public void redo() {
        this.currentState.redo(this.commandStack);
    }

    public void compute(Courier courier) {
        this.currentState.compute(courier, this.courierRouteUpdater, this.session);
    }

    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    public void addDelivery(Courier courier, Timeframe timeframe) {
        this.currentState.addDelivery(this, courier, timeframe, this.commandStack);
    }

    public void selectDelivery(DeliveryRequest deliveryRequest, Courier courier) {
        this.currentState.selectDelivery(this, deliveryRequest, courier);
    }

    public void removeDelivery() {
        this.currentState.removeDelivery(this, this.commandStack);
    }

    public CommandStack getCommandStack() {
        return commandStack;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setSession(Session session) {
        if (this.session != null) {
            this.session.removeObserver(this.sessionObserver);
            this.session.removeObserver(this.view);
        }
        this.session = session;
        this.session.addObserver(this.sessionObserver);
        this.session.addObserver(this.view);
    }

    public void addCourier() {
        this.currentState.addCourier(this.session, this.commandStack);
    }

    public void removeCourier(Courier courier) {
        this.currentState.removeCourier(this.session, courier, this.commandStack);
    }

    public void selectCourier(Courier courier) {
        this.currentState.selectCourier(this, courier);
    }

    public XMLMapParser getMapParser() {
        return this.mapParser;
    }

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

    public boolean canUndo() {
        return this.commandStack.canUndo();
    }

    public boolean canRedo() {
        return this.commandStack.canRedo();
    }

    public void saveSession(File file) {
        this.currentState.saveSession(file, this.session);
    }

    public void loadSession(File file) {
        this.currentState.loadSession(this, file, this.session, this.view);
        if (session != null) {
            this.setCurrentState(new MapLoadedState());
        }
    }

    public void cancelTasks() {
        this.courierRouteUpdater.cancel();
    }

    public void shutdown() {
        this.courierRouteUpdater.shutdown();
    }
}
