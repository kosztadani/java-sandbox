package dev.kosztadani.sandbox.circuitsimulator.electronics;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.utils.SetFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public final class Circuit
    implements Component {

    private final Set<Component> components = SetFactory.newIdentitySet();

    private Circuit() {
    }

    public static Circuit create(final Component... components) {
        return create(Arrays.asList(components));
    }

    public static Circuit create(final Collection<? extends Component> components) {
        Circuit circuit = new Circuit();
        circuit.components.addAll(components);
        return circuit;
    }

    @Override
    public boolean update() {
        boolean result = false;
        boolean updatedNow;
        do {
            updatedNow = updateComponentsOnce();
            result |= updatedNow;
        } while (updatedNow);
        return result;
    }

    private boolean updateComponentsOnce() {
        boolean updated = false;
        for (Component component : components) {
            updated |= component.update();
        }
        return updated;
    }


    @Override
    public boolean shareState() {
        boolean stateChanged = false;
        for (Component component : components) {
            stateChanged |= component.shareState();
        }
        return stateChanged;
    }

    public boolean simulate() {
        boolean result = false;
        boolean stateChangedNow;
        do {
            reset();
            update();
            stateChangedNow = shareState();
            result |= stateChangedNow;
        } while (stateChangedNow);
        return result;
    }

    @Override
    public void reset() {
        for (Component component : components) {
            component.reset();
        }
    }
}
