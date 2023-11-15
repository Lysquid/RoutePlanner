package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandStack that track the history of command applied in the program
 */
public class CommandStack extends Observable {

    private List<Command> commands;
    private int currentIndex;

    /**
     * Construct empty command stack
     */
    public CommandStack() {
        this.commands = new ArrayList<>();
        this.currentIndex = -1;
    }

    /**
     * Applies command and registers in the history
     *
     * @param command command to be applied
     */
    public void add(Command command) {
        while (this.commands.size() > currentIndex + 1) {
            this.commands.remove(this.commands.size() - 1);
        }

        this.currentIndex++;
        this.commands.add(command);

        command.apply();
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    /**
     * Undo the last command applied
     */
    public void undo() {
        if (this.canUndo()) {
            this.commands.get(this.currentIndex).undo();
            this.currentIndex--;
        }
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    /**
     * Redo the last command undone
     */
    public void redo() {
        if (this.canRedo()) {
            this.currentIndex++;
            this.commands.get(this.currentIndex).apply();
        }
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }

    /**
     * @return if there is something to undo
     */
    public boolean canUndo() {
        return this.currentIndex >= 0;
    }

    /**
     * @return if there is something to redo
     */
    public boolean canRedo() {
        return this.currentIndex < this.commands.size() - 1;
    }

    /**
     * Resets the command stack to the empty state
     */
    public void reset() {
        this.currentIndex = -1;
        this.commands = new ArrayList<>();
        this.notifyObservers(EventType.COMMAND_STACK_UPDATE, null);
    }
}
