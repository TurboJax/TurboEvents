package org.turbojax;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.turbojax.event.Cancellable;
import org.turbojax.event.Event;
import org.turbojax.event.EventManager;

class MainTest {
    public static final MyListener LISTENER = new MyListener();

    @BeforeAll
    public static void before() {
        EventManager.register(LISTENER);
    }

    @Test
    void callEvent() {
        final String MSG = "Hello World!";

        // Constructing the event
        Event event = new MyEvent(MSG);

        // Calling the event
        EventManager.call(event);
        Assertions.assertEquals(1, LISTENER.eventsFound.size(), "MyListener#eventsFound must have one event");

        Assertions.assertEquals(MSG, LISTENER.eventsFound.poll(), "Latest Event string must be " + MSG);
        Assertions.assertTrue(LISTENER.eventsFound.isEmpty(), "MyListener#eventsFound must be empty");
        Assertions.assertTrue(LISTENER.cancellableFound.isEmpty(), "MyListener#cancellableFound must be empty");
    }

    @Test
    void testUnregister() {
        final String MSG = "msg";
        final Event event = new MyEvent(MSG);

        // Unregistering the listener
        EventManager.unregister(LISTENER);

        // Calling the event
        EventManager.call(event);
        Assertions.assertNull(LISTENER.eventsFound.poll(), "Unregistered listener should not be able to receive events");

        Assertions.assertTrue(LISTENER.eventsFound.isEmpty(), "MyListener#eventsFound must be empty");
        Assertions.assertTrue(LISTENER.cancellableFound.isEmpty(), "MyListener#cancellableFound must be empty");

        // Re-registering the listener
        EventManager.register(LISTENER);

        // Re-calling the event
        EventManager.call(event);
        Assertions.assertEquals(MSG, LISTENER.eventsFound.poll(), "Unregistered listener should not be able to receive events");

        Assertions.assertTrue(LISTENER.eventsFound.isEmpty(), "MyListener#eventsFound must be empty");
        Assertions.assertTrue(LISTENER.cancellableFound.isEmpty(), "MyListener#cancellableFound must be empty");
    }

    @Test
    void testCancellable() {
        // Creating a cancellable event that shouldn't be canceled.
        Cancellable event = new MyCancellable("cancellable");
        EventManager.call(event);

        Assertions.assertFalse(event.isCancelled(), "This event should not be cancelled!");

        // Creating a cancellable event that should be canceled.
        event = new MyCancellable("cancel_me");
        EventManager.call(event);

        Assertions.assertTrue(event.isCancelled(), "This event should be cancelled!");
    }
}
