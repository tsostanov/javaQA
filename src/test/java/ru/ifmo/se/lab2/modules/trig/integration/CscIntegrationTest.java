package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class CscIntegrationTest {
    @ParameterizedTest
    @MethodSource("cscCases")
    void shouldIntegrateCscWithSinStub(double x, double expected) {
        MathModule sinStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/sin-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        CscModule cscModule = new CscModule(sinStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, cscModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(sinStub).calculate(x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> cscCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/csc-stub.csv");
    }
}
