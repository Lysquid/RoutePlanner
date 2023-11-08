package fr.blazanome.routeplanner.test.controller;

import fr.blazanome.routeplanner.controller.Command;
import fr.blazanome.routeplanner.controller.CommandStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        assertEquals(1, this.counter);
    }


    @Test
    public void testMultipleAdd() {
        this.counter = 0;
        var command1 = new TestCommand(1);
        var command10 = new TestCommand(10);
        var stack = new CommandStack();
        stack.add(command1);
        assertEquals(1, this.counter);
        stack.add(command10);
        assertEquals(11, this.counter);
    }

    @Test
    public void testUndoNothing() {
        this.counter = 0;
        new CommandStack().undo(); // Should not throw exception
    }

    @Test
    public void testNothingToRedo() {
        this.counter = 0;
        new CommandStack().redo(); // Should not throw exception
    }

    @Test
    public void testUndo() {
        this.counter = 0;
        var stack = new CommandStack();
        var command1 = new TestCommand(1);
        stack.add(command1);
        assertEquals(1, this.counter);
        stack.undo();
        assertEquals(0, this.counter);
    }

    @Test
    public void testRedo() {
        this.counter = 0;
        var stack = new CommandStack();
        var command1 = new TestCommand(1);
        stack.add(command1);
        assertEquals(1, this.counter);
        stack.undo();
        assertEquals(0, this.counter);
        stack.redo();
        assertEquals(1, this.counter);
    }

    @Test
    public void testAddAfterUndo() {
        this.counter = 0;
        var command1 = new TestCommand(1);
        var command10 = new TestCommand(10);
        var command100 = new TestCommand(100);
        var stack = new CommandStack();

        stack.add(command1);
        assertEquals(1, this.counter);
        stack.add(command10);
        assertEquals(11, this.counter);
        stack.undo();
        assertEquals(1, this.counter);
        stack.add(command100);
        assertEquals(101, this.counter);
        stack.redo();
        assertEquals(101, this.counter);
    }

}
