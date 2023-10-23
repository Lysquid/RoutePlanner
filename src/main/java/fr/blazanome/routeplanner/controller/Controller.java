package fr.blazanome.routeplanner.controller;

import java.io.File;
import java.util.List;

import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;
import fr.blazanome.routeplanner.model.IMap;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.observer.Observable;
import javafx.scene.control.Button;

public class Controller extends Observable {
    
    public State currentState;
    private CommandStack commandStack;

    private List<Integer> deliveries;

    private IMap map;

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

    public void selectIntersection(Intersection intersection, Button clicked) {
        currentState.selectIntersection(this, intersection, clicked);
    }

    public void addDeliveryAction() {
        this.currentState.addDelivery(this);
    }

    public void addDelivery(Intersection intersection){
        this.deliveries.add(map.getVertexId(intersection));
    }

    public void setMap(IMap map) {
        this.map = map;
    }

    public CommandStack getCommandStack() {
        return commandStack;
    }
}
