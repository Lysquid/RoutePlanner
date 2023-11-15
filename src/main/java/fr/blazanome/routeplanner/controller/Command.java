package fr.blazanome.routeplanner.controller;

/**
 * Command to implement command design pattern
 */
public interface Command {

    /**
     * Apply the command
     */
    void apply();

    /**
     * Undo the command
     */
    void undo();
}
