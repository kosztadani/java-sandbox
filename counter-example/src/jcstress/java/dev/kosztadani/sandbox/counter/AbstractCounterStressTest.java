package dev.kosztadani.sandbox.counter;

import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.infra.results.IIII_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;
import org.openjdk.jcstress.infra.results.L_Result;

import java.util.concurrent.ThreadLocalRandom;

abstract class AbstractCounterStressTest {

    protected abstract Counter createCounter();

    @Outcome(id = {"1, 2", "2, 1"}, expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class Test2ThreadsIncrementAndGet
        extends AbstractCounterStressTest {

        private final Counter counter = createCounter();

        protected void t1(final II_Result r) {
            r.r1 = counter.incrementAndGet();
        }

        protected void t2(final II_Result r) {
            r.r2 = counter.incrementAndGet();
        }
    }


    @Outcome(id = "2", expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class Test2ThreadsIncrement
        extends AbstractCounterStressTest {

        private final Counter counter = createCounter();

        protected void t1() {
            counter.increment();
        }

        protected void t2() {
            counter.increment();
        }

        protected void arbiter(final I_Result r) {
            r.r1 = counter.get();
        }
    }


    @Outcome(id = {"0", "1"}, expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class TestOneThreadIncrementsOtherThreadGets
        extends AbstractCounterStressTest {

        private final Counter counter = createCounter();

        protected void t1() {
            counter.increment();
        }

        protected void t2(final I_Result r) {
            r.r1 = counter.get();
        }
    }

    @Outcome(id = "(1, 2|2, 1), (1, 2|2, 1)", expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class Test2Counters
        extends AbstractCounterStressTest {

        private final Counter counter1 = createCounter();

        private final Counter counter2 = createCounter();

        protected void t1(final IIII_Result r) {
            r.r1 = counter1.incrementAndGet();
        }

        protected void t2(final IIII_Result r) {
            r.r2 = counter1.incrementAndGet();
        }

        protected void t3(final IIII_Result r) {
            r.r3 = counter2.incrementAndGet();
        }

        protected void t4(final IIII_Result r) {
            r.r4 = counter2.incrementAndGet();
        }
    }

    @Outcome(id = "0", expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class TestAtomicityWithExternalSynchronization
        extends AbstractCounterStressTest {

        private final Counter counter = createCounter();

        private final Object lock = new Object();

        protected void t1() {
            synchronized (lock) {
                counter.increment();
                counter.increment();
            }
        }

        protected void t2() {
            synchronized (lock) {
                counter.increment();
                counter.increment();
            }
        }

        protected void t3(final I_Result r) {
            synchronized (lock) {
                r.r1 = counter.get() % 2;
            }
        }
    }

    @Outcome(id = "PASS", expect = Expect.ACCEPTABLE)
    @Outcome(expect = Expect.FORBIDDEN)
    public abstract static class TestCombinations
        extends AbstractCounterStressTest {

        private final Counter counter = createCounter();

        private final int count = ThreadLocalRandom.current().nextInt(20);

        private void randomIncrement() {
            for (int i = 0; i < count; i++) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    counter.incrementAndGet();
                } else {
                    counter.increment();
                }
            }
        }

        protected void t1() {
            randomIncrement();
        }

        protected void t2() {
            randomIncrement();
        }

        protected void t3() {
            randomIncrement();
        }

        protected void t4() {
            randomIncrement();
        }

        @SuppressWarnings("checkstyle:MagicNumber")
        protected void arbiter(final L_Result r) {
            int counterValue = counter.get();
            int expected = count * 4;
            if (counterValue == expected) {
                r.r1 = "PASS";
            } else {
                r.r1 = "FAIL (counter=" + counterValue + ", expected=" + expected + ")";
            }
        }
    }
}
