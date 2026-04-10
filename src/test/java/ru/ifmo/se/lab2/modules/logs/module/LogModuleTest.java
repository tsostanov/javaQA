package ru.ifmo.se.lab2.modules.logs.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class LogModuleTest {
    private static final double EPS = 1.0E-6;
    private final LnModule lnModule = new LnModule(1.0E-10);

    @ParameterizedTest
    @MethodSource("log2Cases")
    void shouldApproximateTabulatedLogarithm(double x, double expected) {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

        assertEquals(expected, logModule.calculate(x), EPS);
    }

    @Test
    void shouldExposeConfiguredBase() {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

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

    private static Stream<Arguments> log2Cases() {
        return CsvTestData.arguments("/testdata/module/logs/log2.csv");
    }
}
