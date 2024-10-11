package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.AbstractComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.JunctionState;

/**
 * Connects two {@link Junction}s together.
 */
public final class Connection
    implements Component {

    private final Port port1;

    private final Port port2;

    private Connection(final Port port1, final Port port2) {
        this.port1 = port1;
        this.port2 = port2;
    }

    public static Connection create() {
        Port port1 = new Port();
        Port port2 = new Port();
        port1.otherPort = port2;
        port2.otherPort = port1;
        Connection connection = new Connection(port1, port2);
        return connection;
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

    private static class Port
        extends AbstractComponentPort {

        private Port otherPort;

        @Override
        public JunctionState output() {
            return otherPort.junction().state();
        }
    }
}
