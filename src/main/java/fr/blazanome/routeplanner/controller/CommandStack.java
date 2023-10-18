package fr.blazanome.routeplanner.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandStack
 */
public class CommandStack {

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
    }

    public void undo() {
        if (this.currentIndex >= 0) {
            this.commands.get(this.currentIndex).undo();
            this.currentIndex--;
        }
    }

    public void redo() {
        if (this.currentIndex < this.commands.size() - 1) {
            this.currentIndex++;
            this.commands.get(this.currentIndex).apply();
        }
    }
}
