package dev.kosztadani.sandbox.counter;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * A counter implementation that delegates to an array of integers laid out such
 * that (hopefully) most accesses by different threads will not share the same
 * cache line. These integers are accessed through a {@link VarHandle} to
 * guarantee atomic and volatile access.
 */
public final class SparseVarHandleCounter
    extends AbstractSparseCounter {

    private static final VarHandle COUNTER_HANDLE = MethodHandles.arrayElementVarHandle(int[].class);

    private final int[] counters;

    /**
     * Construct a new instance with default capacity and sparsity.
     */
    public SparseVarHandleCounter() {
        this(DEFAULT_CAPACITY, DEFAULT_SPARSITY);
    }

    /**
     * Construct a new instance with the given capacity.
     *
     * @param capacity The number of threads that will be able to increment the
     *                 counter concurrently without performance degradation due
     *                 to sharing cache lines.
     */
    public SparseVarHandleCounter(final int capacity) {
        this(capacity, DEFAULT_SPARSITY);
    }

    /**
     * Construct a new instance with the given capacity and sparsity.
     *
     * @param capacity The number of threads that will be able to increment the
     *                 counter concurrently without performance degradation due
     *                 to sharing cache lines.
     * @param sparsity The padding factor for the array where the counters are
     *                 stored. E.g., a value of 32 means that only every 32nd
     *                 element is used as a data store, the rest is padding.
     */
    public SparseVarHandleCounter(final int capacity, final int sparsity) {
        super(capacity, sparsity);

        counters = new int[arraySize()];
    }

    @Override
    public int get() {
        int result = 0;
        synchronized (getLock()) {
            for (int i = 0; i < capacity(); i++) {
                int index = getIndex(i);
                result += (int) COUNTER_HANDLE.get(counters, index);
            }
        }
        return result;
    }

    @Override
    public void increment() {
        int index = getIndexForThread();
        COUNTER_HANDLE.getAndAdd(counters, index, 1);
    }
}
