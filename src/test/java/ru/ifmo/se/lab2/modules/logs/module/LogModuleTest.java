package ru.ifmo.se.lab2.modules.logs.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.MathModule;
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
        assertTrue(Double.isNaN(logModule.calculate(Double.NaN)));
        assertTrue(Double.isNaN(logModule.calculate(Double.POSITIVE_INFINITY)));
    }

    @Test
    void shouldRejectInvalidBase() {
        assertThrows(IllegalArgumentException.class, () -> new LogModule(1.0, lnModule, 1.0E-10));
        assertThrows(IllegalArgumentException.class, () -> new LogModule(0.0, lnModule, 1.0E-10));
        assertThrows(IllegalArgumentException.class, () -> new LogModule(-2.0, lnModule, 1.0E-10));
    }

    @ParameterizedTest
    @CsvSource({
            "2.0,0.5",
            "2.0,3.0",
            "3.0,1.5",
            "10.0,2.5"
    })
    void shouldStayCloseToMathLogarithm(double base, double x) {
        LogModule logModule = new LogModule(base, lnModule, 1.0E-10);

        assertEquals(Math.log(x) / Math.log(base), logModule.calculate(x), EPS);
    }

    @Test
    void shouldCacheLnOfBaseBetweenCalculations() {
        MathModule lnMock = mock(MathModule.class);
        when(lnMock.calculate(2.0)).thenReturn(Math.log(2.0));
        when(lnMock.calculate(16.0)).thenReturn(Math.log(16.0));
        when(lnMock.calculate(64.0)).thenReturn(Math.log(64.0));
        LogModule logModule = new LogModule(2.0, lnMock, 1.0E-10);

        assertEquals(4.0, logModule.calculate(16.0), EPS);
        assertEquals(6.0, logModule.calculate(64.0), EPS);
        verify(lnMock).calculate(2.0);
        verify(lnMock).calculate(16.0);
        verify(lnMock).calculate(64.0);
        verify(lnMock, times(1)).calculate(2.0);
        verifyNoMoreInteractions(lnMock);
    }

    @Test
    void shouldBeZeroAtOneForBaseGreaterThanOne() {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

        assertEquals(0.0, logModule.calculate(1.0), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.5, 0.7})
    void shouldBeNegativeBelowOneForBaseGreaterThanOne(double x) {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

        assertTrue(logModule.calculate(x) < 0.0);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.5, 2.5, 3.0})
    void shouldBePositiveAboveOneForBaseGreaterThanOne(double x) {
        LogModule logModule = new LogModule(2.0, lnModule, 1.0E-10);

        assertTrue(logModule.calculate(x) > 0.0);
    }

    @Test
    void shouldInvertSignClassesForBaseBetweenZeroAndOne() {
        LogModule logModule = new LogModule(0.5, lnModule, 1.0E-10);

        assertTrue(logModule.calculate(0.5) > 0.0);
        assertEquals(0.0, logModule.calculate(1.0), EPS);
        assertTrue(logModule.calculate(2.0) < 0.0);
    }

    private static Stream<Arguments> log2Cases() {
        return CsvTestData.arguments("/testdata/module/logs/log2.csv");
    }
}
