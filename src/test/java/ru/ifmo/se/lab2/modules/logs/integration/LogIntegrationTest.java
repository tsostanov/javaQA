package ru.ifmo.se.lab2.modules.logs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class LogIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double[] POSITIVE_POINTS = {0.2, 0.7, 1.5, 2.5, 3.0, 3.1415926535897931};

    @Test
    void shouldReturnNaNWhenLnReturnsNaNForArgument() {
        MathModule lnModule = mock(MathModule.class);
        when(lnModule.calculate(2.0)).thenReturn(1.0);
        when(lnModule.calculate(3.0)).thenReturn(Double.NaN);
        LogModule logModule = new LogModule(2.0, lnModule, PRECISION);

        assertTrue(Double.isNaN(logModule.calculate(3.0)));
        verify(lnModule).calculate(2.0);
        verify(lnModule).calculate(3.0);
        verifyNoMoreInteractions(lnModule);
    }

    @Test
    void shouldReturnNaNWhenLnReturnsNaNForBase() {
        MathModule lnModule = mock(MathModule.class);
        when(lnModule.calculate(2.0)).thenReturn(Double.NaN);
        when(lnModule.calculate(3.0)).thenReturn(1.0);
        LogModule logModule = new LogModule(2.0, lnModule, PRECISION);

        assertTrue(Double.isNaN(logModule.calculate(3.0)));
        verify(lnModule).calculate(2.0);
        verify(lnModule).calculate(3.0);
        verifyNoMoreInteractions(lnModule);
    }

    @ParameterizedTest
    @MethodSource("logCases")
    void shouldIntegrateLogWithLnModule(double base, double x, double expected) {
        MathModule lnModule = MockitoMathModuleFactory.tabulated("/testdata/module/logs/ln.csv", merge(base, POSITIVE_POINTS));
        LogModule logModule = new LogModule(base, lnModule, PRECISION);

        assertEquals(expected, logModule.calculate(x), EPS);
        if (Double.doubleToLongBits(base) == Double.doubleToLongBits(x)) {
            verify(lnModule, times(2)).calculate(x);
        } else {
            verify(lnModule).calculate(base);
            verify(lnModule).calculate(x);
        }
        verifyNoMoreInteractions(lnModule);
    }

    private static Stream<Arguments> logCases() {
        return Stream.of(
                withBase(2.0, CsvTestData.rows("/testdata/module/logs/log2.csv")),
                withBase(3.0, CsvTestData.rows("/testdata/module/logs/log3.csv")),
                withBase(5.0, CsvTestData.rows("/testdata/module/logs/log5.csv")),
                withBase(10.0, CsvTestData.rows("/testdata/module/logs/log10.csv"))
        ).flatMap(stream -> stream)
                .filter(row -> containsPositivePoint(row.x()))
                .map(row -> Arguments.of(row.base(), row.x(), row.expected()));
    }

    private static Stream<LogCase> withBase(double base, java.util.List<CsvTestData.Row> rows) {
        return rows.stream().map(row -> new LogCase(base, row.x(), row.expected()));
    }

    private static boolean containsPositivePoint(double x) {
        for (double point : POSITIVE_POINTS) {
            if (Double.doubleToLongBits(point) == Double.doubleToLongBits(x)) {
                return true;
            }
        }
        return false;
    }

    private static double[] merge(double base, double[] points) {
        double[] merged = new double[points.length + 1];
        merged[0] = base;
        System.arraycopy(points, 0, merged, 1, points.length);
        return merged;
    }

    private record LogCase(double base, double x, double expected) {
    }
}
