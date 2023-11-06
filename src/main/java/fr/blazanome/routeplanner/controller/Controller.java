package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.algorithm.DjikstraCompleteGraphAlgorithm;
import fr.blazanome.routeplanner.algorithm.TourGenerationAlgorithm;
import fr.blazanome.routeplanner.algorithm.TwoStepTourGenerationAlogrithm;
import fr.blazanome.routeplanner.algorithm.tsp.TSP1;
import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.tools.XMLMapParser;
import fr.blazanome.routeplanner.view.View;

import java.io.File;

public class Controller {

    private final View view;
    private Session session;
    public State currentState;
    private CommandStack commandStack;

    private final TourGenerationAlgorithm tourGenerationAlgorithm;

    private final XMLMapParser mapParser;

    public Controller(View view) {
        this.view = view;
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();

        this.mapParser = new XMLMapParser(new AdjacencyListMap.BuilderFactory());
        this.tourGenerationAlgorithm = new TwoStepTourGenerationAlogrithm(new DjikstraCompleteGraphAlgorithm(),
                new TSP1());
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    public void loadMap(File file) {
        this.currentState.loadMap(this, this.view, file);
    }

    public void undo() {
        this.currentState.undo(this);
    }

    public void redo() {
        this.currentState.redo(this);
    }

    public void compute() {
        this.currentState.compute(this.tourGenerationAlgorithm, this.session);
    }

    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    public void addDelivery(Courier courier, Timeframe timeframe) {
        this.currentState.addDelivery(this, this.view, this.session, courier, timeframe);
    }

    public CommandStack getCommandStack() {
        return commandStack;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void addCourier() {
        this.currentState.addCourier(this.session);
    }

    public void removeCourier(Courier courier) {
        this.currentState.removeCourier(this.session, courier);
    }

    public XMLMapParser getMapParser() {
        return this.mapParser;
    }

    public TourGenerationAlgorithm getTourGenerationAlgorithm() {
        return this.tourGenerationAlgorithm;
    }
}
