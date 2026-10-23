package org.turbojax;

import org.turbojax.event.Cancellable;

public class MyCancellable implements Cancellable {
    private final String msg;
    private boolean cancelled = false;

    public MyCancellable(String msg) {
        this.msg = msg;
    }

    public String msg() {
        return msg;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
