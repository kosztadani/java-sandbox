package dev.kosztadani.sandbox.counter;

/**
 * A counter implementation that delegates to an array of integers laid out such
 * that (hopefully) most accesses by different threads will not share the same
 * cache line. These integers are guarded by locks laid out similarly in another
 * array.
 */
public final class SparseSynchronizedCounter
    extends AbstractSparseCounter {

    private final int[] counters;

    private final Object[] locks;

    /**
     * Construct a new instance with default capacity and sparsity.
     */
    public SparseSynchronizedCounter() {
        this(DEFAULT_CAPACITY, DEFAULT_SPARSITY);
    }

    /**
     * Construct a new instance with the given capacity.
     *
     * @param capacity The number of threads that will be able to increment the
     *                 counter concurrently without performance degradation due
     *                 to sharing cache lines.
     */
    public SparseSynchronizedCounter(final int capacity) {
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
    public SparseSynchronizedCounter(final int capacity, final int sparsity) {
        super(capacity, sparsity);

        counters = new int[arraySize()];

        locks = new Object[arraySize()];
        for (int i = 0; i < arraySize(); i++) {
            locks[i] = new Object();
        }
    }

    @Override
    public int get() {
        int result = 0;
        synchronized (getLock()) {
            for (int i = 0; i < capacity(); i++) {
                int index = getIndex(i);
                result += counters[index];
            }
        }
        return result;
    }

    @Override
    public void increment() {
        int index = getIndexForThread();
        synchronized (locks[index]) {
            counters[index]++;
        }
    }
}
