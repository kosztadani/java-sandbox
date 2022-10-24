package dev.kosztadani.sandbox.counter;

public class VarHandleCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new VarHandleCounter();
    }
}
