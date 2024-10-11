package dev.kosztadani.sandbox.circuitsimulator.electronics.common;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.JunctionState;

public final class HighImpedancePort
    extends AbstractComponentPort {

    @Override
    public JunctionState output() {
        return JunctionState.HIGH_IMPEDANCE;
    }
}
