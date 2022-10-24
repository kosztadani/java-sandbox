package dev.kosztadani.sandbox.counter;

final class ThreadLocalCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new ThreadLocalCounter();
    }
}
