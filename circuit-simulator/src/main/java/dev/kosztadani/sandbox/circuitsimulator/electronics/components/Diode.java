package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.AbstractComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.JunctionState;

/**
 * A diode asserts a positive voltage on its cathode only if a positive voltage is present on its anode.
 */
public final class Diode
    implements Component {

    private Anode anode;

    private Cathode cathode;

    private Diode() {
    }

    public static Diode create() {
        Diode diode = new Diode();
        diode.anode = new Anode();
        diode.cathode = diode.new Cathode();
        return diode;
    }

    public void connectAnode(Junction junction) {
        anode.connect(junction);
    }

    public void connectCathode(Junction junction) {
        cathode.connect(junction);
    }

    @Override
    public boolean update() {
        return cathode.update();
    }

    @Override
    public void reset() {
        anode.reset();
        cathode.reset();
    }

    private static class Anode
        extends AbstractComponentPort {

        @Override
        public JunctionState output() {
            return JunctionState.HIGH_IMPEDANCE;
        }
    }

    private class Cathode
        extends AbstractComponentPort {

        @Override
        public JunctionState output() {
            if (anode.junction().state() == JunctionState.POSITIVE) {
                return JunctionState.POSITIVE;
            } else {
                return JunctionState.HIGH_IMPEDANCE;
            }
        }
    }
}
