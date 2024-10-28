package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public interface Component
    extends Updatable, Resettable {

    default boolean shareState() {
        return false;
    }

}
