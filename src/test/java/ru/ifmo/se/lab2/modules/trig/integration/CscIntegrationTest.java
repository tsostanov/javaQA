package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class CscIntegrationTest {
    private static final String CSC_VALUES = "/testdata/integration/trig/csc-stub.csv";

    @ParameterizedTest
    @MethodSource("cscCases")
    void shouldIntegrateCscWithSinStub(double x, double expected) {
        MathModule sinStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/sin-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        CscModule cscModule = new CscModule(sinStub, TrigIntegrationSupport.PRECISION);

        assertEquals(expected, cscModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(sinStub).calculate(x);
    }

    @Test
    void shouldNotMatchTabulatedCscWhenDependencyIsNotSinModule() {
        double x = -2.3;
        MathModule wrongDependency = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/cos-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        CscModule cscModule = new CscModule(wrongDependency, TrigIntegrationSupport.PRECISION);

        assertNotEquals(CsvTestData.expected(CSC_VALUES, x), cscModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(wrongDependency).calculate(x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> cscCases() {
        return TrigIntegrationSupport.cases(CSC_VALUES);
    }
}
