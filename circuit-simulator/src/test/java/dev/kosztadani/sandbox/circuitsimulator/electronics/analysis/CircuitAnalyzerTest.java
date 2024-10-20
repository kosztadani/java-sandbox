package dev.kosztadani.sandbox.circuitsimulator.electronics.analysis;

import dev.kosztadani.sandbox.circuitsimulator.electronics.Circuit;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Contact;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.PowerSupply;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Relay;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.state.SharedStateRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CircuitAnalyzerTest {

    @Test
    void test() {
        SharedStateRegistry<String> states = SharedStateRegistry.create();
        SharedState rst = states.get("rst");
        SharedState t = states.get("t");
        SharedState o = states.get("o");

        PowerSupply psu = PowerSupply.create();
        Contact rstContact = Contact.createNormallyClosed(rst);
        Contact tContact = Contact.createNormallyClosed(t);
        Contact oContact = Contact.createNormallyOpen(o);
        Relay oRelay = Relay.create(o);

        Junction positive = new Junction();
        Junction negative = new Junction();
        Junction j1 = new Junction();
        Junction j2 = new Junction();

        psu.connectPositive(positive);
        psu.connectNegative(negative);
        rstContact.connectPort1(positive);
        rstContact.connectPort2(j1);
        tContact.connectPort1(j1);
        tContact.connectPort2(j2);
        oContact.connectPort1(j1);
        oContact.connectPort2(j2);
        oRelay.connectPort1(j2);
        oRelay.connectPort2(negative);

        Circuit circuit = Circuit.create(psu, rstContact, tContact, oContact, oRelay);
        CircuitAnalyzer<String> analyzer = new CircuitAnalyzer<>(circuit, states);
        AnalysisResults<String> results = analyzer.analyze();

        System.out.println(results);
    }

}
