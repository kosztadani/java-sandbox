package dev.kosztadani.sandbox.circuitsimulator.electronics.common;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.ComponentPort;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;

public abstract class AbstractComponentPort
    implements ComponentPort {

    private Junction junction = new Junction();

    public void connect(Junction junction) {
        this.junction = junction;
    }

    @Override
    public Junction junction() {
        return junction;
    }
}
