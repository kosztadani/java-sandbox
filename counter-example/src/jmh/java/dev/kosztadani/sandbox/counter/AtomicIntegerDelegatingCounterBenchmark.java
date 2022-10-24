package dev.kosztadani.sandbox.counter;

public class AtomicIntegerDelegatingCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new AtomicIntegerDelegatingCounter();
    }
}
