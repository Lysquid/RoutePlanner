package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.AddCourierCommand;
import fr.blazanome.routeplanner.controller.Command;
import fr.blazanome.routeplanner.controller.CommandStack;
import fr.blazanome.routeplanner.controller.state.IntersectionSelectedState;
import fr.blazanome.routeplanner.controller.state.MapLoadedState;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Session;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCourierCommandTest{
    int counter = 0;

    @Test
    public void testUndo(){
        Courier courier = new Courier();
        Session session = new Session();
        MapLoadedState currentState = new MapLoadedState();
        AddCourierCommand command = new AddCourierCommand(courier, session);
        CommandStack stack = new CommandStack();

        currentState.addCourier(session,stack);
        assertEquals(1,session.getCouriers().size());
        command.undo();
        assertEquals(0,session.getCouriers().size());

    }
}
