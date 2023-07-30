package dev.kosztadani.sandbox.counter;

final class SparseSynchronizedCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new SparseSynchronizedCounter();
    }
}
