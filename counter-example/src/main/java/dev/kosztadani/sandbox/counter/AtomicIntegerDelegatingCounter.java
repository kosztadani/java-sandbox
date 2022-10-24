package dev.kosztadani.sandbox.counter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A counter implementation that simply delegates to {@link AtomicInteger}.
 */
public final class AtomicIntegerDelegatingCounter
    implements Counter {

    private final AtomicInteger delegate = new AtomicInteger();

    @Override
    public int get() {
        return delegate.get();
    }

    @Override
    public void increment() {
        delegate.incrementAndGet();
    }

    @Override
    public int incrementAndGet() {
        return delegate.incrementAndGet();
    }
}
