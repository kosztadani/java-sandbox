package dev.kosztadani.sandbox.counter;

final class AtomicIntegerDelegatingCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new AtomicIntegerDelegatingCounter();
    }
}
