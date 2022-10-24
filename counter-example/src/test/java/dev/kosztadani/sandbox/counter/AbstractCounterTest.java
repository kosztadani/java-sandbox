package dev.kosztadani.sandbox.counter;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for all {@link Counter} implementations.
 */
abstract class AbstractCounterTest {

    /**
     * Create a new instance of the counter to be tested.
     *
     * @return A new counter object.
     */
    abstract Counter createCounter();

    @Test
    void testGetAfterConstruction() {
        Counter counter = createCounter();
        assertEquals(0, counter.get());
    }

    @Test
    void testGetAfterIncrement() {
        Counter counter = createCounter();
        counter.increment();
        assertEquals(1, counter.get());
    }

    @Test
    void testIncrementAndGet() {
        Counter counter = createCounter();
        assertEquals(1, counter.incrementAndGet());
    }

    @Test
    void testIncrementAndIncrementAndGet() {
        Counter counter = createCounter();
        counter.increment();
        assertEquals(2, counter.incrementAndGet());
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    void testManyIncrements() {
        Counter counter = createCounter();
        Random random = new Random(0);
        for (int i = 0; i < 100; i++) {
            if (random.nextBoolean()) {
                counter.increment();
            } else {
                counter.incrementAndGet();
            }
        }
        assertEquals(100, counter.get());
    }
}
