package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class SecIntegrationTest {
    private static final String SEC_VALUES = "/testdata/integration/trig/sec-stub.csv";

    @ParameterizedTest
    @MethodSource("secCases")
    void shouldIntegrateSecWithCosStub(double x, double expected) {
        MathModule cosStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/cos-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        SecModule secModule = new SecModule(cosStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, secModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(cosStub).calculate(x);
    }

    @Test
    void shouldNotMatchTabulatedSecWhenDependencyIsNotCosModule() {
        double x = -2.3;
        MathModule wrongDependency = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/sin-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        SecModule secModule = new SecModule(wrongDependency, TrigIntegrationSupport.PRECISION);

        assertNotEquals(CsvTestData.expected(SEC_VALUES, x), secModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(wrongDependency).calculate(x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> secCases() {
        return TrigIntegrationSupport.cases(SEC_VALUES);
    }
}
