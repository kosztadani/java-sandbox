package dev.kosztadani.sandbox.counter;

final class SparseVarHandleCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new SparseVarHandleCounter();
    }
}
