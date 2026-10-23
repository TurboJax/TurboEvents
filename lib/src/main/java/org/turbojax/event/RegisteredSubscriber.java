package org.turbojax.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public record RegisteredSubscriber(Listener owner, Method method, boolean ignoreCancelled) {
    public void consume(Event event) {
        // Skipping canceled events if we don't ignore them
        if (event instanceof Cancellable cancellable && cancellable.isCancelled() && !ignoreCancelled) return;

        // Passing the event through the method
        try {
            method.invoke(owner, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}