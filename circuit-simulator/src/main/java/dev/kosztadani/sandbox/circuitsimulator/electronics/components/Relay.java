package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.HighImpedancePort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.ComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

/**
 * One relay coil, which gets energized (activating its shared state) if opposing polarity is applied across it.
 */
public final class Relay
    implements Component {

    private final SharedState state;

    private ComponentPort port1;

    private ComponentPort port2;

    private Relay(final SharedState state) {
        this.state = state;
    }

    public static Relay create(final SharedState state) {
        Relay relay = new Relay(state);
        relay.port1 = new HighImpedancePort();
        relay.port2 = new HighImpedancePort();
        return relay;
    }

    public void connectPort1(final Junction junction) {
        port1.connect(junction);
    }

    public void connectPort2(final Junction junction) {
        port2.connect(junction);
    }

    @Override
    public boolean update() {
        State oldState = state.get();
        State newState = newState();
        state.set(newState);
        return newState != oldState;
    }

    @Override
    public void reset() {
        port1.reset();
        port2.reset();
    }

    private State newState() {
        if (port1.junction().state().isOpposite(port2.junction().state())) {
            return State.ACTIVE;
        } else {
            return State.INACTIVE;
        }
    }
}
