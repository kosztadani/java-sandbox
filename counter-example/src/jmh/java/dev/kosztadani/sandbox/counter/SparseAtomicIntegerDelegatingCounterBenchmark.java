package dev.kosztadani.sandbox.counter;

public class SparseAtomicIntegerDelegatingCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new SparseAtomicIntegerDelegatingCounter();
    }
}
