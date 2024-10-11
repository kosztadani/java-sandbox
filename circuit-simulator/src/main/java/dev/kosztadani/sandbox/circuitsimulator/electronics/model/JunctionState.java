package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public enum JunctionState {
    POSITIVE,
    NEGATIVE,
    HIGH_IMPEDANCE;

    public boolean isOpposite(final JunctionState other) {
        return ((this == POSITIVE && other == NEGATIVE) ||
            (this == NEGATIVE && other == POSITIVE));
    }

    public boolean isConnected() {
        return this != HIGH_IMPEDANCE;
    }
}
