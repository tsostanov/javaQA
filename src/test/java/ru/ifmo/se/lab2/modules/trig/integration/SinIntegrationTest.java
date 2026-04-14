package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class SinIntegrationTest {
    private static final String SIN_VALUES = "/testdata/integration/trig/sin-stub.csv";

    @ParameterizedTest
    @MethodSource("sinCases")
    void shouldIntegrateSinWithCosStub(double x, double expected) {
        double[] cosArguments = TrigIntegrationSupport.transformedArguments(TrigIntegrationSupport.TRIG_POINTS);
        MathModule cosStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/cos-stub.csv", cosArguments);
        SinModule sinModule = new SinModule(cosStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, sinModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(cosStub).calculate(Math.PI / 2.0 - x);
    }

    @Test
    void shouldNotMatchTabulatedSinWhenDependencyIsNotCosModule() {
        double x = -2.3;
        MathModule wrongDependency = MockitoMathModuleFactory.constant(0.25);
        SinModule sinModule = new SinModule(wrongDependency, TrigIntegrationSupport.PRECISION);

        assertNotEquals(CsvTestData.expected(SIN_VALUES, x), sinModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(wrongDependency).calculate(Math.PI / 2.0 - x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> sinCases() {
        return TrigIntegrationSupport.cases(SIN_VALUES);
    }
}
