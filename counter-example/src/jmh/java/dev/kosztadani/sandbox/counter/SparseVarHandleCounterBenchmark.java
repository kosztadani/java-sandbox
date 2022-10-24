package dev.kosztadani.sandbox.counter;

public class SparseVarHandleCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new SparseVarHandleCounter();
    }
}
