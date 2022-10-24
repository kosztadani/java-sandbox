package dev.kosztadani.sandbox.counter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A counter implementation that delegates to a set of thread-local
 * {@link AtomicInteger}s.
 */
public final class ThreadLocalCounter
    implements Counter {

    private final List<AtomicInteger> counters = new CopyOnWriteArrayList<>();

    private final ThreadLocal<AtomicInteger> threadLocalCounter =
        ThreadLocal.withInitial(() -> {
            AtomicInteger newCounter = new AtomicInteger();
            counters.add(newCounter);
            return newCounter;
        });

    private final Object lock = new Object();

    @Override
    public int get() {
        int result = 0;
        synchronized (lock) {
            for (AtomicInteger counter : counters) {
                result += counter.get();
            }
        }
        return result;
    }

    @Override
    public void increment() {
        threadLocalCounter.get().incrementAndGet();
    }

    @Override
    public int incrementAndGet() {
        synchronized (lock) {
            increment();
            return get();
        }
    }
}
