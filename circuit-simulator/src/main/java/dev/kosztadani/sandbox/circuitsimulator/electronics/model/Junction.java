package dev.kosztadani.sandbox.circuitsimulator.electronics.model;

import dev.kosztadani.sandbox.circuitsimulator.utils.SetFactory;

import java.util.Set;

/**
 * A point in a circuit, to which {@link ComponentPort}s can be connected.
 */
public final class Junction
    implements Resettable {

    private final Set<ComponentPort> positive = SetFactory.newIdentitySet();

    private final Set<ComponentPort> negative = SetFactory.newIdentitySet();

    public JunctionState state() {
        if (positive.isEmpty() && negative.isEmpty()) {
            return JunctionState.HIGH_IMPEDANCE;
        } else if (positive.isEmpty()) {
            return JunctionState.NEGATIVE;
        } else if (negative.isEmpty()) {
            return JunctionState.POSITIVE;
        } else {
            throw new ShortedJunctionException(toString());
        }
    }

    public boolean set(final ComponentPort port, JunctionState state) {
        return switch (state) {
            case POSITIVE -> positive(port);
            case NEGATIVE -> negative(port);
            case HIGH_IMPEDANCE -> highImpedance(port);
        };
    }

    public boolean positive(final ComponentPort port) {
        return negative.remove(port) | positive.add(port);
    }

    public boolean negative(final ComponentPort port) {
        return positive.remove(port) | negative.add(port);
    }

    public boolean highImpedance(final ComponentPort port) {
        return positive.remove(port) | negative.remove(port);
    }

    @Override
    public void reset() {
        positive.clear();
        negative.clear();
    }

    @Override
    public String toString() {
        return "Junction{" +
            "positive=" + positive +
            ", negative=" + negative +
            '}';
    }
}
