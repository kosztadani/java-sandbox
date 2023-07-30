package dev.kosztadani.sandbox.counter;

public class LongAdderDelegatingCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new LongAdderDelegatingCounter();
    }

}
