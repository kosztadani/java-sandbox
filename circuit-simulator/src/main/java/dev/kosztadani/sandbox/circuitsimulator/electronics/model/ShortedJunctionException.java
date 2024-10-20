package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public final class ShortedJunctionException
    extends IllegalStateException {

    public ShortedJunctionException(final String junctionDescription) {
        super("Junction is shorted: " + junctionDescription);
    }
}
