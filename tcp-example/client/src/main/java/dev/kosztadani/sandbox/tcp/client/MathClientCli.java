package dev.kosztadani.sandbox.tcp.client;

import dev.kosztadani.sandbox.tcp.common.protocol.MathOperationType;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;
import dev.kosztadani.sandbox.tcp.common.protocol.MathResponse;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Command line interface for a math client.
 */
final class MathClientCli {

    private final MathClient client;

    private final Scanner scanner;

    MathClientCli(final MathClient client, final InputStream input) {
        this.client = client;
        scanner = new Scanner(input);
    }

    void run() {
        System.out.println("Input operations, e.g., \"5 + 3\":" + System.lineSeparator());
        while (true) {
            MathRequest request = readInput();
            MathResponse response = client.request(request);
            System.out.println(response.result());
        }
    }

    private MathRequest readInput() {
        System.out.print(">>> ");
        int left = scanner.nextInt();
        MathOperationType operation = parseOperation(scanner.next());
        int right = scanner.nextInt();
        return new MathRequest(operation, left, right);
    }

    private MathOperationType parseOperation(final String operation) {
        return switch (operation.trim()) {
            case "+" -> MathOperationType.ADD;
            case "-" -> MathOperationType.SUBTRACT;
            case "*" -> MathOperationType.MULTIPLY;
            case "/" -> MathOperationType.DIVIDE;
            default -> throw new RuntimeException("Invalid operation: " + operation);
        };
    }
}
