package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.model.*;

/**
 * Command to add a courier in the session
 */
public class AddCourierCommand implements Command {
    private Courier courier;
    private Session session;

    public AddCourierCommand(Courier courier, Session session) {
        this.courier = courier;
        this.session = session;
    }

    /**
     * Apply command : add courier to session
     */
    @Override
    public void apply() {
        this.session.addCourier(courier);
    }

    /**
     * Undo command : remove courier from session
     */
    @Override
    public void undo() {
        this.session.removeCourier(courier);
    }
}
