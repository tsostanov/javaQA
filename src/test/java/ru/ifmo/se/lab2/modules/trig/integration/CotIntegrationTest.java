package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.support.stubs.ModuleStubFactory;

class CotIntegrationTest {
    @ParameterizedTest
    @MethodSource("cotCases")
    void shouldIntegrateCotWithSinAndCosStubs(double x, double expected) {
        MathModule cosStub = ModuleStubFactory.cosStub(TrigIntegrationSupport.TRIG_POINTS);
        MathModule sinStub = ModuleStubFactory.sinStub(TrigIntegrationSupport.TRIG_POINTS);
        CotModule cotModule = new CotModule(cosStub, sinStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, cotModule.calculate(x), TrigIntegrationSupport.EPS);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> cotCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/cot-stub.csv");
    }
}
