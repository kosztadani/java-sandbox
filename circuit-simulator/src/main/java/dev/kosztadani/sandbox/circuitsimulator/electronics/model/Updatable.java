package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

public interface Updatable {

    /**
     * Updates the states.
     *
     * @return whether the state has changed.
     */
    boolean update();

}
