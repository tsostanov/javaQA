package ru.ifmo.se.lab2.modules.logs.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class LogBottomUpIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;

    @ParameterizedTest
    @MethodSource("logCases")
    void shouldBuildLogModulesOnTopOfRealLn(double base, double x, double expected) {
        LnModule ln = new LnModule(PRECISION);
        LogModule log = new LogModule(base, ln, PRECISION);

        assertEquals(expected, log.calculate(x), EPS);
    }

    private static Stream<Arguments> logCases() {
        return Stream.of(
                withBase(2.0, "/testdata/module/logs/log2.csv"),
                withBase(3.0, "/testdata/module/logs/log3.csv"),
                withBase(5.0, "/testdata/module/logs/log5.csv"),
                withBase(10.0, "/testdata/module/logs/log10.csv")
        ).flatMap(stream -> stream);
    }

    private static Stream<Arguments> withBase(double base, String resourcePath) {
        return CsvTestData.rows(resourcePath).stream()
                .map(row -> Arguments.of(base, row.x(), row.expected()));
    }
}
