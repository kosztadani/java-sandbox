package dev.kosztadani.sandbox.circuitsimulator;

import dev.kosztadani.sandbox.circuitsimulator.electronics.Circuit;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Contact;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Diode;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.PowerSupply;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Relay;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircuitTest {

    @Test
    void test() {
        SharedState rst = new SharedState(State.INACTIVE);
        SharedState t = new SharedState(State.INACTIVE);
        SharedState o = new SharedState(State.INACTIVE);

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

        circuit.update();

        assertEquals(State.ACTIVE, o.get());
    }

    @Test
    void test2() {
        SharedState o = new SharedState(State.INACTIVE);

        PowerSupply psu = PowerSupply.create();
        Diode diode = Diode.create();
        Relay relay = Relay.create(o);

        Junction positive = new Junction();
        Junction negative = new Junction();
        Junction j1 = new Junction();

        psu.connectPositive(positive);
        psu.connectNegative(negative);
        diode.connectAnode(positive);
        diode.connectCathode(j1);
        relay.connectPort1(j1);
        relay.connectPort2(negative);

        Circuit circuit = Circuit.create(psu, diode, relay);

        circuit.update();

        assertEquals(State.ACTIVE, o.get());
    }

    @Test
    void test3() {
        SharedState o = new SharedState(State.INACTIVE);

        PowerSupply psu = PowerSupply.create();
        Diode diode = Diode.create();
        Relay relay = Relay.create(o);

        Junction positive = new Junction();
        Junction negative = new Junction();
        Junction j1 = new Junction();

        psu.connectPositive(positive);
        psu.connectNegative(negative);
        diode.connectAnode(j1);
        diode.connectCathode(positive);
        relay.connectPort1(j1);
        relay.connectPort2(negative);

        Circuit circuit = Circuit.create(psu, diode, relay);

        circuit.update();

        assertEquals(State.INACTIVE, o.get());
    }
}
