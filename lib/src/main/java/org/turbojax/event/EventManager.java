package org.turbojax.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private static final Map<Class<? extends Event>, List<RegisteredSubscriber>> HANDLERS = new HashMap<>();

    public static void call(Event event) {
        List<RegisteredSubscriber> subs = HANDLERS.get(event.getClass());
        if (subs == null) return;

        subs.forEach(s -> s.consume(event));
    }

    /**
     * Registers all the event subscribers in a listener.<br>
     * Looks for methods with the {@link Subscribe} annotation.  Make sure to use it.
     * @param listener A {@link Listener} to get events from.
     */
    public static void register(Listener listener) {
        for (Method m : listener.getClass().getDeclaredMethods()) {
            // Looking for @Subscribe annotations
            if (!m.isAnnotationPresent(Subscribe.class)) continue;
            Subscribe sub = m.getAnnotation(Subscribe.class);

            // Handling constraints
            if (m.getParameterCount() != 1) throw new InvalidListenerException("Listeners must have one parameter.");
            if (!Event.class.isAssignableFrom(m.getParameterTypes()[0])) throw new InvalidListenerException("The parameter for the listener is " + m.getParameterTypes()[0].toGenericString() + ", which is not an Event.");
            if (!m.getReturnType().equals(Void.TYPE)) throw new InvalidListenerException("Listeners must have the `void` return type.  Found '" + m.getReturnType().getName() + "'");

            // Storing the method
            //noinspection unchecked
            Class<? extends Event> clazz = (Class<? extends Event>) m.getParameterTypes()[0];
            List<RegisteredSubscriber> eventHandlers = HANDLERS.computeIfAbsent(clazz, c -> new ArrayList<>());
            eventHandlers.add(new RegisteredSubscriber(listener, m, sub.ignoreCancelled()));
        }
    }

    /**
     * Unregisters a listener.<br>
     * Checks for matching listener objects using {@link Object#equals}.
     * @param listener The listener to remove event handlers for.
     */
    public static void unregister(Listener listener) {
        HANDLERS.values().forEach(s -> s.removeIf(listener::owns));
    }
}
