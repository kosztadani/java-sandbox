package dev.kosztadani.sandbox.tcp.integration;

import dev.kosztadani.sandbox.tcp.client.MathClient;
import dev.kosztadani.sandbox.tcp.client.MathClientConnector;
import dev.kosztadani.sandbox.tcp.common.protocol.MathOperationType;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;
import dev.kosztadani.sandbox.tcp.common.threading.ThreadFactory;
import dev.kosztadani.sandbox.tcp.server.MathServer;
import dev.kosztadani.sandbox.tcp.server.MathServerConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class IntegrationTest {

    private MathServerConnector serverConnector;

    private MathClientConnector clientConnector;

    private MathClient client;

    @BeforeEach
    void setup() throws IOException {
        InetAddress localhost = InetAddress.getLocalHost();
        InetSocketAddress listenAddress = new InetSocketAddress(localhost, 0);
        serverConnector = MathServerConnector.create();
        ThreadFactory.newCriticalThread(serverConnector, "math-server").start();
        MathServer server = serverConnector.listenOn(listenAddress);
        clientConnector = MathClientConnector.create();
        ThreadFactory.newCriticalThread(clientConnector, "math-client").start();
        client = clientConnector.connect(server.getAddress());
    }

    @SuppressWarnings("checkstyle:EmptyBlock")
    @AfterEach
    void teardown() throws Exception {
        try (AutoCloseable o1 = serverConnector;
             AutoCloseable o2 = clientConnector) {
            // no logic, just close the resources
        }
    }

    @Test
    void testAddition() {
        MathResponse response = client.request(new MathRequest(MathOperationType.ADD, 5, 3));
        assertEquals(8, response.result());
    }

    @Test
    void testSubtraction() {
        MathResponse response = client.request(new MathRequest(MathOperationType.SUBTRACT, 5, 3));
        assertEquals(2, response.result());
    }

    @Test
    void testMultiplication() {
        MathResponse response = client.request(new MathRequest(MathOperationType.MULTIPLY, 5, 3));
        assertEquals(15, response.result());
    }

    @Test
    void testDivision() {
        MathResponse response = client.request(new MathRequest(MathOperationType.DIVIDE, 5, 3));
        assertEquals(1, response.result());
    }
}
