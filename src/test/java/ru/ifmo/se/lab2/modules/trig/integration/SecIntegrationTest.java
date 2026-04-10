package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.support.stubs.ModuleStubFactory;

class SecIntegrationTest {
    @ParameterizedTest
    @MethodSource("secCases")
    void shouldIntegrateSecWithCosStub(double x, double expected) {
        MathModule cosStub = ModuleStubFactory.cosStub(TrigIntegrationSupport.TRIG_POINTS);
        SecModule secModule = new SecModule(cosStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, secModule.calculate(x), TrigIntegrationSupport.EPS);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> secCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/sec-stub.csv");
    }
}
