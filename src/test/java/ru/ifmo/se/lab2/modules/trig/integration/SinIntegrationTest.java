package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.stubs.ModuleStubFactory;

class SinIntegrationTest {
    @ParameterizedTest
    @MethodSource("sinCases")
    void shouldIntegrateSinWithCosStub(double x, double expected) {
        double[] cosArguments = TrigIntegrationSupport.transformedArguments(TrigIntegrationSupport.TRIG_POINTS);
        MathModule cosStub = ModuleStubFactory.cosStub(cosArguments);
        SinModule sinModule = new SinModule(cosStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, sinModule.calculate(x), TrigIntegrationSupport.EPS);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> sinCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/sin-stub.csv");
    }
}
