package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class CotIntegrationTest {
    private static final String COT_VALUES = "/testdata/integration/trig/cot-stub.csv";

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

    @Test
    void shouldNotMatchTabulatedCotWhenCosAndSinDependenciesAreSwapped() {
        double x = -2.3;
        MathModule cosStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/cos-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        MathModule sinStub = MockitoMathModuleFactory.tabulated("/testdata/integration/trig/sin-stub.csv", TrigIntegrationSupport.TRIG_POINTS);
        CotModule cotModule = new CotModule(sinStub, cosStub, TrigIntegrationSupport.PRECISION);

        assertNotEquals(CsvTestData.expected(COT_VALUES, x), cotModule.calculate(x), TrigIntegrationSupport.EPS);
        verify(sinStub).calculate(x);
        verify(cosStub).calculate(x);
    }

    private static java.util.stream.Stream<org.junit.jupiter.params.provider.Arguments> cotCases() {
        return TrigIntegrationSupport.cases(COT_VALUES);
    }
}
