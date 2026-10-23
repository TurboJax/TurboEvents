package org.turbojax;

import org.turbojax.event.Listener;
import org.turbojax.event.Subscribe;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyListener implements Listener {
    public final Queue<String> eventsFound = new ArrayDeque<>();
    public final Queue<String> cancellableFound = new ArrayDeque<>();

    @Subscribe
    public void onEvent(MyEvent event) {
        eventsFound.add(event.msg());
    }

    @Subscribe
    public void onCancellable(MyCancellable event) {
        cancellableFound.add(event.msg());

        if (event.msg().equals("cancel_me")) {
            event.setCancelled(true);
        }
    }
}