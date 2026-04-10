package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class CotIntegrationTest {
    @ParameterizedTest
    @MethodSource("cotCases")
    void shouldIntegrateCotWithSinAndCosStubs(double x, double expected) {
        MathModule cosStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/cos-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        MathModule sinStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/sin-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        CotModule cotModule = new CotModule(cosStub, sinStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, cotModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(cosStub).calculate(x);
        verify(sinStub).calculate(x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> cotCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/cot-stub.csv");
    }
}
