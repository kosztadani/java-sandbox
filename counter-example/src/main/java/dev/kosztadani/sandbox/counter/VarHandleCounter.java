package dev.kosztadani.sandbox.counter;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * A counter implementation that uses a single integer accessed through a
 * {@link VarHandle} to guarantee atomic access.
 */
public final class VarHandleCounter
    implements Counter {

    private static final VarHandle COUNTER_HANDLE;

    static {
        try {
            COUNTER_HANDLE = MethodHandles.lookup()
                .findVarHandle(
                    VarHandleCounter.class,
                    "counter",
                    int.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    @SuppressWarnings("unused") // accessed via COUNTER_HANDLE
    private int counter;

    /**
     * Creates a new counter initialized with 0.
     */
    public VarHandleCounter() {
    }

    @Override
    public int get() {
        return (int) COUNTER_HANDLE.getVolatile(this);
    }

    @Override
    public void increment() {
        COUNTER_HANDLE.getAndAdd(this, 1);
    }

    @Override
    public int incrementAndGet() {
        return (int) COUNTER_HANDLE.getAndAdd(this, 1) + 1;
    }
}
