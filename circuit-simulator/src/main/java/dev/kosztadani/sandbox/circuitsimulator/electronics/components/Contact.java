package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.AbstractComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.common.ContactState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.JunctionState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

/**
 * A contact which bridges its ports together in the {@link ContactState#CLOSED} state.
 */
public final class Contact
    implements Component {

    private final SharedState state;

    private final ContactState defaultState;

    private Port port1;

    private Port port2;

    private Contact(final SharedState state, final ContactState defaultState) {
        this.state = state;
        this.defaultState = defaultState;
    }

    public static Contact createNormallyClosed(final SharedState state) {
        return create(state, ContactState.CLOSED);
    }

    public static Contact createNormallyOpen(final SharedState state) {
        return create(state, ContactState.OPEN);
    }

    public static Contact create(final SharedState state, final ContactState defaultState) {
        Contact contact = new Contact(state, defaultState);
        contact.port1 = contact.new Port();
        contact.port2 = contact.new Port();
        contact.port1.otherPort = contact.port2;
        contact.port2.otherPort = contact.port1;
        return contact;
    }

    public void connectPort1(final Junction junction) {
        port1.connect(junction);
    }

    public void connectPort2(final Junction junction) {
        port2.connect(junction);
    }

    @Override
    public boolean update() {
        return port1.update() | port2.update();
    }

    @Override
    public void reset() {
        port1.reset();
        port2.reset();
    }

    private class Port
        extends AbstractComponentPort {

        private Port otherPort;

        @Override
        public JunctionState output() {
            if (contactState() == ContactState.CLOSED) {
                return otherPort.junction().state();
            } else {
                return JunctionState.HIGH_IMPEDANCE;
            }
        }

        private ContactState contactState() {
            State currentState = state.get();
            if (currentState == State.INACTIVE) {
                return defaultState;
            } else {
                return defaultState.opposite();
            }
        }
    }
}
