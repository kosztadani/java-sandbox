package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

import java.util.Objects;

/**
 * Mutable wrapper around a {@link State}.
 */
public final class SharedState {

    private State value;

    public SharedState() {
        this(State.INACTIVE);
    }

    public SharedState(final State initialValue) {
        value = initialValue;
    }

    public void set(final State newValue) {
        value = newValue;
    }

    public State get() {
        return value;
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
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "SharedState{" +
            "value=" + value +
            '}';
    }
}
