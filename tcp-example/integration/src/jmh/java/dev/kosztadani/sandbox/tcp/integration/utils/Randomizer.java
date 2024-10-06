package dev.kosztadani.sandbox.tcp.integration.utils;

import dev.kosztadani.sandbox.tcp.common.protocol.MathOperationType;
import dev.kosztadani.sandbox.tcp.common.protocol.MathRequest;

import java.util.concurrent.ThreadLocalRandom;

public final class Randomizer {

    private static final MathOperationType[] OPERATIONS = MathOperationType.values();

    public MathRequest randomRequest() {
        MathOperationType operation = randomOperation();
        MathRequest request = new MathRequest(
            operation,
            randomInt(),
            randomRightOperand(operation)
        );
        return request;
    }

    private MathOperationType randomOperation() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return OPERATIONS[random.nextInt(OPERATIONS.length)];
    }

    private int randomInt() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt();
    }

    private int randomNonZero() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (true) {
            int value = random.nextInt();
            if (value != 0) {
                return value;
            }
        }
    }

    private int randomRightOperand(final MathOperationType operation) {
        if (operation == MathOperationType.DIVIDE) {
            return randomNonZero();
        } else {
            return randomInt();
        }
    }
}
