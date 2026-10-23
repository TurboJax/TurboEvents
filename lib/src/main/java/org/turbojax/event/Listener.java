package org.turbojax.event;

public interface Listener {
    /**
     * Determines whether this subscriber is owned by this listener or not.
     * Defaults to a {@link Object#equals} comparison.
     * @param subscriber The subscriber to validate.
     * @return {@code true} if the subscriber is owned by this listener. {@code false} otherwise.
     */
    default boolean owns(RegisteredSubscriber subscriber) {
        return subscriber.owner().equals(this);
    }
}