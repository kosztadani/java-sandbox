package dev.kosztadani.sandbox.tcp.integration;

import dev.kosztadani.sandbox.tcp.client.MathClient;
import dev.kosztadani.sandbox.tcp.client.MathClientConnector;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;
import dev.kosztadani.sandbox.tcp.common.threading.ThreadFactory;
import dev.kosztadani.sandbox.tcp.integration.utils.Randomizer;
import dev.kosztadani.sandbox.tcp.server.MathServer;
import dev.kosztadani.sandbox.tcp.server.MathServerConnector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
public class PerformanceTest {

    private final Randomizer randomizer = new Randomizer();

    private MathServerConnector serverConnector;

    private MathClientConnector clientConnector;

    private MathClient client;

    @Setup
    public void setup() throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        InetSocketAddress listenAddress = new InetSocketAddress(localhost, 0);
        serverConnector = MathServerConnector.create();
        ThreadFactory.newCriticalThread(serverConnector, "math-server").start();
        MathServer server = serverConnector.listenOn(listenAddress);
        clientConnector = MathClientConnector.create();
        ThreadFactory.newCriticalThread(clientConnector, "math-client").start();
        client = clientConnector.connect(server.getAddress());
    }

    @TearDown
    public void tearDown() throws IOException {
        serverConnector.close();
        clientConnector.close();
    }

    @Benchmark
    public int singleRequest() {
        return client.request(randomizer.randomRequest()).result();
    }

    @Benchmark
    public int thousandRequests() {
        @SuppressWarnings("unchecked") CompletableFuture<MathResponse>[] results = new CompletableFuture[1000];
        for (int i = 0; i < 1000; i++) {
            results[i] = client.submit(randomizer.randomRequest());
        }
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += results[i].join().result();
        }
        return sum;
    }

    @Benchmark
    public int thousandRequestsPreJoin() {
        @SuppressWarnings("unchecked") CompletableFuture<MathResponse>[] results = new CompletableFuture[1000];
        for (int i = 0; i < 1000; i++) {
            results[i] = client.submit(randomizer.randomRequest());
        }
        CompletableFuture.allOf(results).join();
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += results[i].join().result();
        }
        return sum;
    }

    @Benchmark
    public int thousandRequestsBusyWait() {
        @SuppressWarnings("unchecked") CompletableFuture<MathResponse>[] results = new CompletableFuture[1000];
        for (int i = 0; i < 1000; i++) {
            results[i] = client.submit(randomizer.randomRequest());
        }
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            while (!results[i].isDone()) {
                Thread.onSpinWait();
            }
            sum += results[i].join().result();
        }
        return sum;
    }

}
