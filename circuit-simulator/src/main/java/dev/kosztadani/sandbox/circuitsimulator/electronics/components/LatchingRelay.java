package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.HighImpedancePort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.ComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

/**
 * A relay which remembers its last position and has separate pairs of ports for switchor from one state to the other.
 */
public final class LatchingRelay
    implements Component {

    private final SharedState state;

    private ComponentPort activatorPort1;

    private ComponentPort activatorPort2;

    private ComponentPort deActivatorPort1;

    private ComponentPort deActivatorPort2;

    private LatchingRelay(final SharedState state) {
        this.state = state;
    }

    public static LatchingRelay create(final SharedState state) {
        LatchingRelay relay = new LatchingRelay(state);
        relay.activatorPort1 = new HighImpedancePort();
        relay.activatorPort2 = new HighImpedancePort();
        relay.deActivatorPort1 = new HighImpedancePort();
        relay.deActivatorPort2 = new HighImpedancePort();
        return relay;
    }

    public void connectActivatorPort1(final Junction junction) {
        activatorPort1.connect(junction);
    }

    public void connectActivatorPort2(final Junction junction) {
        activatorPort2.connect(junction);
    }

    public void connectDeActivatorPort1(final Junction junction) {
        deActivatorPort1.connect(junction);
    }

    public void connectDeActivatorPort2(final Junction junction) {
        deActivatorPort2.connect(junction);
    }

    @Override
    public boolean update() {
        State oldState = state.get();
        State newState;
        if (activatorActive() && !deActivatorActive()) {
            newState = State.ACTIVE;
        } else if (!activatorActive() && deActivatorActive()) {
            newState = State.INACTIVE;
        } else {
            newState = oldState;
        }
        state.set(newState);
        return newState != oldState;
    }

    private boolean activatorActive() {
        return activatorPort1.junction().state().isOpposite(activatorPort2.output());
    }

    private boolean deActivatorActive() {
        return deActivatorPort1.junction().state().isOpposite(deActivatorPort2.output());
    }
}
