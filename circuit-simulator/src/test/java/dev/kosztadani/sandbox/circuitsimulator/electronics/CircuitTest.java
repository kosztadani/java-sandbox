package dev.kosztadani.sandbox.circuitsimulator.electronics;

import dev.kosztadani.sandbox.circuitsimulator.electronics.common.ContactState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Contact;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Diode;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.PowerSupply;
import dev.kosztadani.sandbox.circuitsimulator.electronics.components.Relay;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.Junction;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.SharedState;
import dev.kosztadani.sandbox.circuitsimulator.electronics.model.State;
import dev.kosztadani.sandbox.circuitsimulator.electronics.state.SharedStateRegistry;
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

        circuit.simulate();

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

        circuit.simulate();

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

        circuit.simulate();

        assertEquals(State.INACTIVE, o.get());
    }


    @Test
    void test4() {
        SharedStateRegistry<String> states = SharedStateRegistry.create();
        SharedState c = states.get("c");
        SharedState x = states.get("x");
        SharedState r = states.get("r");

        PowerSupply psu = PowerSupply.create();
        Contact c1 = Contact.create(c, ContactState.CLOSED);
        Contact x1 = Contact.create(x, ContactState.CLOSED);
        Contact x2 = Contact.create(x, ContactState.CLOSED);
        Relay r1 = Relay.create(r);

        Junction positive = new Junction();
        Junction negative = new Junction();
        Junction j1 = new Junction();
        Junction j2 = new Junction();
        Junction j3 = new Junction();

        psu.connectPositive(positive);
        psu.connectNegative(negative);
        c1.connectPort1(positive);
        c1.connectPort2(j1);
        x1.connectPort1(j1);
        x1.connectPort2(j2);
        x2.connectPort1(j2);
        x2.connectPort2(j3);
        r1.connectPort1(j3);
        r1.connectPort2(negative);

        Circuit circuit = Circuit.create(psu, c1, x1, x2, r1);

        circuit.simulate();
        assertEquals(State.ACTIVE, r.get());

        c.set(State.ACTIVE);
        circuit.simulate();
        assertEquals(State.INACTIVE, r.get());
    }
}
