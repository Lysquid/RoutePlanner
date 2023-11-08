package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.AddDeliveryCommand;
import fr.blazanome.routeplanner.model.Courier;
import fr.blazanome.routeplanner.model.Intersection;
import fr.blazanome.routeplanner.model.Timeframe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddDeliveryCommandTest {

    @Test
    public void testUndoApply(){
        Courier courier = new Courier();
        Intersection selectedIntersection = new Intersection(1.0,2.0);
        Timeframe timeframe = Timeframe.H8;
        AddDeliveryCommand command = new AddDeliveryCommand(courier,selectedIntersection,timeframe);

        command.apply();
        assertEquals(1,courier.getRequests().size());
        command.undo();
        assertEquals(0,courier.getRequests().size());
        command.apply();
        assertEquals(1,courier.getRequests().size());
        command.apply();
        assertEquals(2,courier.getRequests().size());
        command.undo();
        assertEquals(1,courier.getRequests().size());
    }
}
