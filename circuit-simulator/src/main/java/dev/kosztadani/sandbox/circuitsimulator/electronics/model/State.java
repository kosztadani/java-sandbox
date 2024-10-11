package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public enum State {
    INACTIVE,
    ACTIVE;

    public State opposite() {
        return switch (this) {
            case INACTIVE -> ACTIVE;
            case ACTIVE -> INACTIVE;
        };
    }
}
