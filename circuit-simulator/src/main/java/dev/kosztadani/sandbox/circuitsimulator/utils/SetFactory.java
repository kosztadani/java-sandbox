package dev.kosztadani.sandbox.circuitsimulator.utils;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class SetFactory {

    private SetFactory() {
    }

    public static <T> Set<T> newIdentitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }
}
