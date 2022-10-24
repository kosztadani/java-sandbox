package dev.kosztadani.sandbox.counter;

final class VarHandleCounterTest
    extends AbstractCounterTest {

    @Override
    Counter createCounter() {
        return new VarHandleCounter();
    }
}
