package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

import java.util.Objects;

/**
 * Mutable wrapper around a {@link State}.
 */
public final class SharedState
    implements Resettable {

    private final State initialValue;

    private State value;

    public SharedState() {
        this(State.INACTIVE);
    }

    public SharedState(final State initialValue) {
        this.initialValue = initialValue;
        value = initialValue;
    }

    public void set(final State newValue) {
        value = newValue;
    }

    public State get() {
        return value;
    }

    @Override
    public void reset() {
        value = initialValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SharedState that = (SharedState) o;
        return initialValue == that.initialValue && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialValue, value);
    }

    @Override
    public String toString() {
        return "SharedState{" +
            "initialValue=" + initialValue +
            ", value=" + value +
            '}';
    }
}
