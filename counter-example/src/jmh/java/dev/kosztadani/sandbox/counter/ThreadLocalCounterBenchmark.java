package dev.kosztadani.sandbox.counter;

public class ThreadLocalCounterBenchmark
    extends AbstractCounterBenchmark {

    @Override
    Counter createCounter() {
        return new ThreadLocalCounter();
    }

}
