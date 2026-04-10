package ru.ifmo.se.lab2.modules.logs.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class LnModuleTest {
    private static final double EPS = 1.0E-6;
    private static final String LN_VALUES = "/testdata/module/logs/ln.csv";
    private final LnModule lnModule = new LnModule(1.0E-10);

    @ParameterizedTest
    @MethodSource("lnCases")
    void shouldApproximateTabulatedLog(double x, double expected) {
        assertEquals(expected, lnModule.calculate(x), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    void shouldReturnNaNOutsideDomain(double x) {
        assertTrue(Double.isNaN(lnModule.calculate(x)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.POSITIVE_INFINITY, Double.NaN})
    void shouldReturnNaNForNonFiniteInput(double x) {
        assertTrue(Double.isNaN(lnModule.calculate(x)));
    }

    private static Stream<Arguments> lnCases() {
        return CsvTestData.arguments(LN_VALUES);
    }
}
