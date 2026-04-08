package ru.ifmo.se.lab2.modules.logs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LnModuleTest {
    private static final double EPS = 1.0E-6;
    private final LnModule lnModule = new LnModule(1.0E-10);

    @ParameterizedTest
    @ValueSource(doubles = {0.25, 0.5, 1.0, 1.5, 2.0, 5.0, 10.0})
    void shouldApproximateMathLog(double x) {
        assertEquals(Math.log(x), lnModule.calculate(x), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, 0.0})
    void shouldReturnNaNOutsideDomain(double x) {
        assertTrue(Double.isNaN(lnModule.calculate(x)));
    }
}
