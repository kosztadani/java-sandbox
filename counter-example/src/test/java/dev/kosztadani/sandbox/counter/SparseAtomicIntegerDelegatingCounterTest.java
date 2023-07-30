package dev.kosztadani.sandbox.counter;

final class SparseAtomicIntegerDelegatingCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new SparseAtomicIntegerDelegatingCounter();
    }
}
