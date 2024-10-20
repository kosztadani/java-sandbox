package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public interface ComponentPort
    extends Updatable, Resettable {

    void connect(Junction junction);

    JunctionState output();

    Junction junction();

    default boolean update() {
        return junction().set(this, output());
    }

    default void reset() {
        junction().highImpedance(this);
    }
}
