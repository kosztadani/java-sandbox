package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.HighImpedancePort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.ComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

/**
 * Two relay coils, one terminal of each is implicitly connected to positive and negative voltage, respectively.
 * The other terminals are joined, therefore, if either positive or negative voltage is connected to its port,
 * the coil is energized.
 */
public final class DualRelay
    implements Component {

    private final SharedState state;

    private ComponentPort port;

    private DualRelay(final SharedState state) {
        this.state = state;
    }

    public static DualRelay create(final SharedState state) {
        DualRelay dualRelay = new DualRelay(state);
        dualRelay.port = new HighImpedancePort();
        return dualRelay;
    }

    public void connectPort(final Junction junction) {
        port.connect(junction);
    }

    @Override
    public boolean update() {
        State oldState = state.get();
        State newState = newState();
        state.set(newState);
        return newState != oldState;
    }

    private State newState() {
        if (port.junction().state().isConnected()) {
            return State.ACTIVE;
        } else {
            return State.INACTIVE;
        }
    }
}
