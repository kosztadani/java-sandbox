package dev.kosztadani.sandbox.counter;

public class SparseSynchronizedCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new SparseSynchronizedCounter();
    }
}
