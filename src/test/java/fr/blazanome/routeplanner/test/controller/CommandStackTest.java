package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.Command;
import fr.blazanome.routeplanner.controller.CommandStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CommandStackTest {
    int counter = 0;
    private class TestCommand implements Command {


        private int incr;

        public TestCommand(int incr) {
            this.incr = incr;
        }

        @Override
        public void apply() {
            counter += this.incr;
        }

        @Override
        public void undo() {
            counter -= this.incr;
        }
    }

    @Test
    public void testAdd() {
        this.counter = 0;
        var command = new TestCommand(1);
        var stack = new CommandStack();

        stack.add(command);

        assertEquals(this.counter, 1);
    }


    @Test
    public void testMultipleAdd() {
        this.counter = 0;
        var command1 = new TestCommand(1);
        var command10 = new TestCommand(10);
        var stack = new CommandStack();
        stack.add(command1);
        assertEquals(counter, 1);
        stack.add(command10);
        assertEquals(this.counter, 11);
    }

    @Test
    public void testUndoNothing() {
        this.counter = 0;
        new CommandStack().undo(); // Should not throw expection
    }

    @Test
    public void testNothingToRedo() {
        this.counter = 0;
        new CommandStack().redo(); // Should not throw excpetion
    }

    @Test
    public void testAddAfterUndo() {
        this.counter = 0;
        var command1 = new TestCommand(1);
        var command10 = new TestCommand(10);
        var command100 = new TestCommand(100);
        var stack = new CommandStack();

        stack.add(command1);
        assertEquals(counter, 1);
        stack.add(command10);
        assertEquals(counter, 11);
        stack.undo();
        assertEquals(counter, 1);
        stack.add(command100);
        assertEquals(counter, 101);
        stack.redo();
        assertEquals(this.counter, 101);
    }

}
