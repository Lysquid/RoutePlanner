package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandStack
 */
public class CommandStack extends Observable {

    private List<Command> commands;
    private int currentIndex;

    public CommandStack() {
        this.commands = new ArrayList<>();
        this.currentIndex = -1;
    }

    public void add(Command command) {
        while (this.commands.size() > currentIndex + 1) {
            this.commands.remove(this.commands.size() - 1);
        }

        this.currentIndex++;
        this.commands.add(command);

        command.apply();
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    public void undo() {
        if (this.canUndo()) {
            this.commands.get(this.currentIndex).undo();
            this.currentIndex--;
        }
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    public void redo() {
        if (this.canRedo()) {
            this.currentIndex++;
            this.commands.get(this.currentIndex).apply();
        }
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    public boolean canUndo() {
        return this.currentIndex >= 0;
    }

    public boolean canRedo() {
        return this.currentIndex < this.commands.size() - 1;
    }
}
