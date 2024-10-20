package dev.kosztadani.sandbox.circuitsimulator.electronics.analysis;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Component;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.ShortedJunctionException;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;
import dev.kosztadani.sandbox.circuitsimulator.electronics.state.SharedStateRegistry;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CircuitAnalyzer<K> {

    private final Component circuit;

    private final SharedStateRegistry<K> registry;

    private final List<K> inputs;

    private final List<K> outputs;

    private final List<Set<Observation>> observedOutputs = new ArrayList<>();

    private final Set<Map<K, State>> shorts = new HashSet<>();

    public CircuitAnalyzer(final Component circuit, final SharedStateRegistry<K> registry, final Collection<K> inputs, final Collection<K> outputs) {
        this.circuit = circuit;
        this.registry = registry;
        this.inputs = new ArrayList<>(inputs);
        this.outputs = new ArrayList<>(outputs);
        for (int i = 0; i < outputs.size(); i++) {
            observedOutputs.add(EnumSet.noneOf(Observation.class));
        }
    }

    public CircuitAnalyzer(final Component circuit, final SharedStateRegistry<K> registry, final Collection<K> keys) {
        this(circuit, registry, keys, keys);
    }

    public CircuitAnalyzer(final Component circuit, final SharedStateRegistry<K> registry) {
        this(circuit, registry, registry.keys());
    }

    public AnalysisResults<K> analyze() {
        for (long i = 0; i < (1L << inputs.size()); i++) {
            analyzeInput(i);
        }
        return computeResults();
    }

    private AnalysisResults<K> computeResults() {
        Map<K, StateResult> results = new HashMap<>();
        for (int i = 0; i < outputs.size(); i++) {
            K key = outputs.get(i);
            if (observedOutputs.get(i).contains(Observation.CHANGED)) {
                results.put(key, StateResult.CHANGED);
            } else {
                results.put(key, StateResult.ALWAYS_UNCHANGED);
            }
        }
        return new AnalysisResults<>(results, shorts);
    }

    private void analyzeInput(final long input) {
        prepareInputs(input);
        State[] before = readOutputs();
        try {
            circuit.update();
            State[] after = readOutputs();
            recordObservations(before, after);
        } catch (ShortedJunctionException e) {
            recordShort(input);
        }
        circuit.reset();
    }

    private void prepareInputs(final long input) {
        BitSet bs = BitSet.valueOf(new long[]{input});
        for (int i = 0; i < inputs.size(); i++) {
            registry.write(inputs.get(i), mapToState(bs.get(i)));
        }
    }

    private State mapToState(final boolean value) {
        return value ? State.ACTIVE : State.INACTIVE;
    }

    private State[] readOutputs() {
        State[] result = new State[outputs.size()];
        for (int i = 0; i < outputs.size(); i++) {
            result[i] = registry.read(outputs.get(i));
        }
        return result;
    }

    private void recordObservations(final State[] before, final State[] after) {
        for (int i = 0; i < outputs.size(); i++) {
            Set<Observation> observations = observedOutputs.get(i);
            if (before[i] == after[i]) {
                observations.add(Observation.UNCHANGED);
            } else {
                observations.add(Observation.CHANGED);
            }
        }
    }

    private void recordShort(final long input) {
        prepareInputs(input);
        Map<K, State> inputStates = new HashMap<>();
        for (K key : inputs) {
            inputStates.put(key, registry.read(key));
        }
        shorts.add(inputStates);
    }

    private enum Observation {
        UNCHANGED,
        CHANGED
    }
}
