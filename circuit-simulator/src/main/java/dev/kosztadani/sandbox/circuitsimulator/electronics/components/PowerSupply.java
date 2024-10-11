package dev.kosztadani.sandbox.circuitsimulator.electronics.components;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.AbstractComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.JunctionState;

public final class PowerSupply
    implements Component {

    private final Port positive;

    private final Port negative;

    private PowerSupply(final Port positive, final Port negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public static PowerSupply create() {
        return new PowerSupply(new Port(JunctionState.POSITIVE), new Port(JunctionState.NEGATIVE));
    }

    public void connectPositive(final Junction junction) {
        positive.connect(junction);
    }

    public void connectNegative(final Junction junction) {
        negative.connect(junction);
    }

    @Override
    public boolean update() {
        return positive.update() | negative.update();
    }

    private static class Port extends AbstractComponentPort {

        private final JunctionState voltage;

        Port(final JunctionState voltage) {
            this.voltage = voltage;
        }

        @Override
        public JunctionState output() {
            return voltage;
        }
    }
}
