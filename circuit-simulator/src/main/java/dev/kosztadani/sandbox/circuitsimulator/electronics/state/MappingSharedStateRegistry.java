package dev.kosztadani.sandbox.circuitsimulator.electronics.state;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MappingSharedStateRegistry<K>
    implements SharedStateRegistry<K> {

    private final Map<K, SharedState> states = new HashMap<>();

    @Override
    public SharedState get(final K key, final State initialValue) {
        return states.computeIfAbsent(key, ignored -> new SharedState(initialValue));
    }

    @Override
    public Set<K> keys() {
        return states.keySet();
    }

    public Set<Map.Entry<K, SharedState>> entrySet() {
        return states.entrySet();
    }

    @Override
    public void reset() {
        for (SharedState state : states.values()) {
            state.reset();
        }
    }
}
