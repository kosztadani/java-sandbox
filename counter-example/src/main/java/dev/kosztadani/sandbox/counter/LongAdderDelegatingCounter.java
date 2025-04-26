package dev.kosztadani.sandbox.counter;

import java.util.concurrent.atomic.LongAdder;

/**
 * A counter implementation that simply delegates to {@link LongAdder}.
 */
public final class LongAdderDelegatingCounter
    implements Counter {

    private final LongAdder delegate = new LongAdder();

    /**
     * Creates a new counter initialized with 0.
     */
    public LongAdderDelegatingCounter() {
    }

    @Override
    public int get() {
        return delegate.intValue();
    }

    @Override
    public void increment() {
        delegate.increment();
    }

    @Override
    public int incrementAndGet() {
        synchronized (this) {
            delegate.increment();
            return delegate.intValue();
        }
    }
}
