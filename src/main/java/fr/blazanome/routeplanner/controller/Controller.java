package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Session;
import fr.blazanome.routeplanner.view.View;

import java.io.File;

public class Controller {

    private final View view;
    private Session session;
    public State currentState;
    private CommandStack commandStack;
    private IMap map;

    public Controller(View view) {
        this.view = view;
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();
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
        this.currentState.compute(this.session);
    }

    public void selectIntersection(Intersection intersection) {
        this.currentState.selectIntersection(this, this.view, intersection);
    }

    public void addDelivery() {
        this.currentState.addDelivery(this, this.view, this.session);
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

}
