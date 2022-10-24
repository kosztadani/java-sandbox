package dev.kosztadani.sandbox.counter;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;
import org.openjdk.jcstress.infra.results.L_Result;

import java.util.concurrent.ThreadLocalRandom;

public final class SparseSynchronizedCounterStressTest {

    private static Counter newCounter() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int capacity = random.nextInt(1, 5);
        int sparsity = random.nextInt(1, 128);
        return new SparseSynchronizedCounter(capacity, sparsity);
    }

    @JCStressTest
    @State
    public static class Test2ThreadsIncrementAndGet
        extends AbstractCounterStressTest.Test2ThreadsIncrementAndGet {

        @Override
        protected Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        public void t1(final II_Result r) {
            super.t1(r);
        }

        @Override
        @Actor
        public void t2(final II_Result r) {
            super.t2(r);
        }
    }

    @JCStressTest
    @State
    public static class Test2ThreadsIncrement
        extends AbstractCounterStressTest.Test2ThreadsIncrement {

        @Override
        protected Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        public void t1() {
            super.t1();
        }

        @Override
        @Actor
        public void t2() {
            super.t2();
        }

        @Override
        @Arbiter
        public void arbiter(final I_Result r) {
            super.arbiter(r);
        }
    }


    @JCStressTest
    @State
    public static class TestOneThreadIncrementsOtherThreadGets
        extends AbstractCounterStressTest.TestOneThreadIncrementsOtherThreadGets {

        @Override
        protected Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        public void t1() {
            super.t1();
        }

        @Override
        @Actor
        public void t2(final I_Result r) {
            super.t2(r);
        }
    }

    @JCStressTest
    @State
    public static class Test2Counters
        extends AbstractCounterStressTest.Test2Counters {

        @Override
        public Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        public void t1(final IIII_Result r) {
            super.t1(r);
        }

        @Override
        @Actor
        public void t2(final IIII_Result r) {
            super.t2(r);
        }

        @Override
        @Actor
        public void t3(final IIII_Result r) {
            super.t3(r);
        }

        @Override
        @Actor
        public void t4(final IIII_Result r) {
            super.t4(r);
        }
    }

    @JCStressTest
    @State
    public static class TestAtomicityWithExternalSynchronization
        extends AbstractCounterStressTest.TestAtomicityWithExternalSynchronization {

        @Override
        public Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        protected void t1() {
            super.t1();
        }

        @Override
        @Actor
        protected void t2() {
            super.t2();
        }

        @Override
        @Actor
        protected void t3(final I_Result r) {
            super.t3(r);
        }
    }


    @JCStressTest
    @State
    public static class TestCombinations
        extends AbstractCounterStressTest.TestCombinations {

        @Override
        public Counter createCounter() {
            return newCounter();
        }

        @Override
        @Actor
        public void t1() {
            super.t1();
        }

        @Override
        @Actor
        public void t2() {
            super.t2();
        }

        @Override
        @Actor
        public void t3() {
            super.t3();
        }

        @Override
        @Actor
        public void t4() {
            super.t4();
        }

        @Override
        @Arbiter
        public void arbiter(final L_Result r) {
            super.arbiter(r);
        }
    }
}
