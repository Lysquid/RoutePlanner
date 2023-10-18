package fr.blazanome.routeplanner.controller;

/**
 * Command
 */
public interface Command {

    public void apply();

    public void undo();
}
