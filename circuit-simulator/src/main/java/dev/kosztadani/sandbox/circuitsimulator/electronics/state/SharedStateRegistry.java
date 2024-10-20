package dev.kosztadani.sandbox.circuitsimulator.electronics.state;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Resettable;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

import java.util.Map;
import java.util.Set;

public interface SharedStateRegistry<K>
    extends Resettable {

    static <K> SharedStateRegistry<K> create() {
        return new MappingSharedStateRegistry<K>();
    }

    SharedState get(K key, State initialValue);

    Set<K> keys();

    Set<Map.Entry<K, SharedState>> entrySet();

    default SharedState get(K key) {
        return get(key, State.INACTIVE);
    }

    default State read(K key) {
        return get(key).get();
    }

    default State read(K key, State initialValue) {
        return get(key, initialValue).get();
    }

    default void write(K key, State value) {
        get(key).set(value);
    }

    default void write(K key, State initialValue, State value) {
        get(key, initialValue).set(value);
    }
}
