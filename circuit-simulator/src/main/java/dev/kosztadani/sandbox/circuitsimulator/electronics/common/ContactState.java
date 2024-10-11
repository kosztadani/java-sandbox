package dev.kosztadani.sandbox.circuitsimulator.electronics.common;

public enum ContactState {
    OPEN,
    CLOSED;

    public ContactState opposite() {
        return switch (this) {
            case OPEN -> CLOSED;
            case CLOSED -> OPEN;
        };
    }
}
