package fr.blazanome.routeplanner.controller;

import java.io.File;

import fr.blazanome.routeplanner.controller.state.NoMapState;
import fr.blazanome.routeplanner.controller.state.State;

public class Controller {
    
    private State currentState;
    private CommandStack commandStack;

    public Controller() {
        this.currentState = new NoMapState();
        this.commandStack = new CommandStack();
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

    public CommandStack getCommandStack() {
        return commandStack;
    }
}
