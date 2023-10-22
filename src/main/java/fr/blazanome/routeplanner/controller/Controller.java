package fr.blazanome.routeplanner.controller;

import java.io.File;

import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.observer.Observable;

public class Controller extends Observable {
    
    public State currentState;
    private CommandStack commandStack;

    public Controller() {
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();
    }

    public void setCurrentState(State newState) {
        this.currentState = newState;
        this.notifyObservers(this.currentState);
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

    public void selectIntersection(Intersection intersection) {
        currentState.selectIntersection(this, intersection);
    }

    public CommandStack getCommandStack() {
        return commandStack;
    }
}
