package fr.blazanome.routeplanner.controller;

import fr.blazanome.routeplanner.model.*;

public class AddCourierCommand implements Command {
    private Courier courier;
    private Session session;

    public AddCourierCommand(Courier courier, Session session) {
        this.courier = courier;
        this.session = session;
    }

    public void apply() {
        session.addCourier(courier);
    }

    public void undo() {
        session.removeCourier(courier);
    }
}
