package fr.blazanome.routeplanner.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Segment;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.view.View;

public class Controller {

    private View view;
    private Session session;
    public State currentState;
    private CommandStack commandStack;

    private List<Segment> currentPath;

    private List<Integer> deliveries;

    private IMap map;

    public Controller(View view) {
        this.view = view;
        this.session = new Session();
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();
        this.deliveries = new ArrayList<>();
        this.currentPath = new ArrayList<>();
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
    }

    public void loadMap(File file) {
        this.currentState.loadMap(this, file);
    }

    public void undo() {
        this.currentState.undo(this);
    }

    public void redo() {
        this.currentState.redo(this);
    }

    public void compute() {
        this.currentState.compute(this);
    }

    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    public void addDeliveryAction() {
        this.currentState.addDelivery(this, this.view);
    }

    public void addDelivery(Intersection intersection){
        this.deliveries.add(this.session.getMap().getVertexId(intersection));
    }

    public Session getSession() {
        return this.session;
    }

    public CommandStack getCommandStack() {
        return commandStack;
    }

    public State getCurrentState() {
        return currentState;
    }

    public List<Integer> getDeliveries() {
        return deliveries;
    }

    public List<Segment> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(List<Segment> currentPath) {
        this.currentPath = currentPath;
    }
}
