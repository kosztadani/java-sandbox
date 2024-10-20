package dev.kosztadani.sandbox.circuitsimulator.electronics.analysis;

import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;

import java.util.Map;
import java.util.Set;

public record AnalysisResults<K>(Map<K, StateResult> results, Set<Map<K, State>> shortingInputs) {

}
