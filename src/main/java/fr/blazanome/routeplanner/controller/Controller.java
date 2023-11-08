package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
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
    public State currentState;
    private CommandStack commandStack;

    private Observer sessionObserver;
    private Observer courierObserver;

    private final TourGenerationAlgorithm tourGenerationAlgorithm;

    private final XMLMapParser mapParser;

    public Controller(View view) {
        this.view = view;
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();

        this.mapParser = new XMLMapParser(new AdjacencyListMap.BuilderFactory());
        this.tourGenerationAlgorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1());

        this.sessionObserver = Observers.typed(Session.class, this::onSessionChange);
        this.courierObserver = Observers.typed(Courier.class, this::onCourierChange);
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
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
        this.currentState.compute(courier, this.tourGenerationAlgorithm, this.session);
    }

    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    public void addDelivery(Courier courier, Timeframe timeframe) {
        this.currentState.addDelivery(this.view, this.session, courier, timeframe, this.commandStack);
    }

    public void removeDelivery(Courier courier, Timeframe timeframe) {
        this.currentState.removeDelivery(this.view, this.session, courier, timeframe, this.commandStack);
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

    public XMLMapParser getMapParser() {
        return this.mapParser;
    }

    public TourGenerationAlgorithm getTourGenerationAlgorithm() {
        return this.tourGenerationAlgorithm;
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
            default -> {}
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
            default -> {}
        }
    }
}
