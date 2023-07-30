package dev.kosztadani.sandbox.counter;

final class LongAdderDelegatingCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new LongAdderDelegatingCounter();
    }
}
