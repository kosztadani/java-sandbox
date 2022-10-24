package dev.kosztadani.sandbox.counter;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
abstract class AbstractCounterBenchmark {

    private static final String GROUP_1_THREAD_INCREMENT = "c01i";

    private static final String GROUP_16_THREADS_INCREMENT = "c16i";

    private static final String GROUP_1_THREAD_INCREMENT_GET = "c01ig";

    private static final String GROUP_16_THREADS_INCREMENT_GET = "c16ig";

    private final Counter counter = createCounter();

    /**
     * Abstract method for creating a new counter.
     *
     * @return A new instance of the counter implementation to benchmark.
     */
    abstract Counter createCounter();

    //////////////////// 1 thread increment() ////////////////////

    @Benchmark
    @Group(GROUP_1_THREAD_INCREMENT)
    public void counter01i01() {
        counter.increment();
    }

    //////////////////// 1 thread incrementAndGet() //////////////

    @Benchmark
    @Group(GROUP_1_THREAD_INCREMENT_GET)
    public void counter01ig01(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    //////////////////// 16 thread increment() ///////////////////

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i01() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i02() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i03() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i04() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i05() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i06() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i07() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i08() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i09() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i10() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i11() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i12() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i13() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i14() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i15() {
        counter.increment();
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT)
    public void counter16i16() {
        counter.increment();
    }

    //////////////////// 16 thread incrementAndGet() /////////////

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig01(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig02(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig03(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig04(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig05(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig06(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig07(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig08(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig09(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig10(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig11(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig12(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig13(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig14(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig15(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }

    @Benchmark
    @Group(GROUP_16_THREADS_INCREMENT_GET)
    public void counter16ig16(final Blackhole bh) {
        bh.consume(counter.incrementAndGet());
    }
}
