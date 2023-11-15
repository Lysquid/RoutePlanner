package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.AddCourierCommand;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCourierCommandTest {

    @Test
    public void testUndoApply() {
        Courier courier = new Courier(1);
        Session session = new Session();
        AddCourierCommand command = new AddCourierCommand(courier, session);

        command.apply();
        assertEquals(1, session.getCouriers().size());
        command.undo();
        assertEquals(0, session.getCouriers().size());
        command.apply();
        assertEquals(1, session.getCouriers().size());
        command.apply();
        assertEquals(2, session.getCouriers().size());
        command.undo();
        assertEquals(1, session.getCouriers().size());
    }

    @Test
    public void testReversedCommand() {
        Courier courier = new Courier(1);
        Session session = new Session();
        session.addCourier(courier);
        AddCourierCommand command = new AddCourierCommand(courier, session);
        ReverseCommand reverseCommand = new ReverseCommand(command);

        assertEquals(1, session.getCouriers().size());
        reverseCommand.apply();
        assertEquals(0, session.getCouriers().size());
        reverseCommand.undo();
        assertEquals(1, session.getCouriers().size());
    }
}
