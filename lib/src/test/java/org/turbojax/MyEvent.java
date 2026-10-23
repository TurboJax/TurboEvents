package org.turbojax;

import org.turbojax.event.Event;

public record MyEvent(String msg) implements Event {}
