package ru.ifmo.se.lab2.modules.logs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogModuleTest {
    private static final double EPS = 1.0E-6;
    private final LnModule lnModule = new LnModule(1.0E-10);

    @Test
    void shouldApproximateLogarithm() {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

        assertEquals(Math.log(8.0) / Math.log(2.0), logModule.calculate(8.0), EPS);
        assertEquals(2.0, logModule.getBase(), 0.0);
    }

    @Test
    void shouldReturnNaNOutsideDomain() {
        LogModule logModule = new LogModule(10.0, lnModule, 1.0E-10);

        assertTrue(Double.isNaN(logModule.calculate(0.0)));
        assertTrue(Double.isNaN(logModule.calculate(-5.0)));
    }

    @Test
    void shouldRejectInvalidBase() {
        assertThrows(IllegalArgumentException.class, () -> new LogModule(1.0, lnModule, 1.0E-10));
        assertThrows(IllegalArgumentException.class, () -> new LogModule(0.0, lnModule, 1.0E-10));
        assertThrows(IllegalArgumentException.class, () -> new LogModule(-2.0, lnModule, 1.0E-10));
    }
}
