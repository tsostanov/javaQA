package ru.ifmo.se.lab2.modules.logs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class LogIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double[] POSITIVE_POINTS = {0.2, 0.7, 1.5, 2.5, 3.0, 3.1415926535897931};
    private final LnModule lnModule = new LnModule(PRECISION);

    @ParameterizedTest
    @MethodSource("logCases")
    void shouldIntegrateLogWithLnModule(double base, double x, double expected) {
        LogModule logModule = new LogModule(base, lnModule, PRECISION);

        assertEquals(expected, logModule.calculate(x), EPS);
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

    private record LogCase(double base, double x, double expected) {
    }
}
