package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.AddRequestCommand;
import fr.blazanome.routeplanner.controller.ReverseCommand;
import fr.blazanome.routeplanner.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddDeliveryCommandTest {

    @Test
    public void testUndoApply() {
        Courier courier = new Courier(1);
        Intersection selectedIntersection = new Intersection(1, 1.0, 2.0);
        Timeframe timeframe = Timeframe.H8;
        AddRequestCommand command = new AddRequestCommand(courier, new DeliveryRequest(selectedIntersection, timeframe));

        command.apply();
        assertEquals(1, courier.getRequests().size());
        command.undo();
        assertEquals(0, courier.getRequests().size());
        command.apply();
        assertEquals(1, courier.getRequests().size());
        command.apply();
        assertEquals(2, courier.getRequests().size());
        command.undo();
        assertEquals(1, courier.getRequests().size());
    }

    @Test
    public void testReversedCommand() {
        Courier courier = new Courier(1);
        Intersection selectedIntersection = new Intersection(1, 1.0, 2.0);
        Timeframe timeframe = Timeframe.H8;
        DeliveryRequest deliveryRequest = new DeliveryRequest(selectedIntersection, timeframe);
        courier.addDelivery(deliveryRequest);
        AddRequestCommand command = new AddRequestCommand(courier, deliveryRequest);
        ReverseCommand reverseCommand = new ReverseCommand(command);

        assertEquals(1, courier.getRequests().size());
        reverseCommand.apply();
        assertEquals(0, courier.getRequests().size());
        reverseCommand.undo();
        assertEquals(1, courier.getRequests().size());
    }
}
