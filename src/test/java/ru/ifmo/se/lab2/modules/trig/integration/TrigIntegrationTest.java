package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.stubs.ModuleStubFactory;

class TrigIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double[] TRIG_POINTS = {-2.3, -1.2, -0.7};

    @ParameterizedTest
    @MethodSource("sinCases")
    void shouldIntegrateSinWithCosStub(double x, double expected) {
        double[] cosArguments = transformedArguments(TRIG_POINTS);
        MathModule cosStub = ModuleStubFactory.cosStub(cosArguments);
        SinModule sinModule = new SinModule(cosStub, PRECISION);

        assertEquals(expected, sinModule.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("secCases")
    void shouldIntegrateSecWithCosStub(double x, double expected) {
        MathModule cosStub = ModuleStubFactory.cosStub(TRIG_POINTS);
        SecModule secModule = new SecModule(cosStub, PRECISION);

        assertEquals(expected, secModule.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("cscCases")
    void shouldIntegrateCscWithSinStub(double x, double expected) {
        MathModule sinStub = ModuleStubFactory.sinStub(TRIG_POINTS);
        CscModule cscModule = new CscModule(sinStub, PRECISION);

        assertEquals(expected, cscModule.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("cotCases")
    void shouldIntegrateCotWithSinAndCosStubs(double x, double expected) {
        MathModule cosStub = ModuleStubFactory.cosStub(TRIG_POINTS);
        MathModule sinStub = ModuleStubFactory.sinStub(TRIG_POINTS);
        CotModule cotModule = new CotModule(cosStub, sinStub, PRECISION);

        assertEquals(expected, cotModule.calculate(x), EPS);
    }

    private static double[] transformedArguments(double[] points) {
        double[] result = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            result[i] = Math.PI / 2.0 - points[i];
        }
        return result;
    }

    private static Stream<Arguments> sinCases() {
        return CsvTestData.rows("/testdata/integration/trig/sin-stub.csv").stream()
                .filter(row -> containsPoint(row.x()))
                .map(row -> Arguments.of(row.x(), row.expected()));
    }

    private static Stream<Arguments> secCases() {
        return filteredCases("/testdata/integration/trig/sec-stub.csv");
    }

    private static Stream<Arguments> cscCases() {
        return filteredCases("/testdata/integration/trig/csc-stub.csv");
    }

    private static Stream<Arguments> cotCases() {
        return filteredCases("/testdata/integration/trig/cot-stub.csv");
    }

    private static boolean containsPoint(double x) {
        for (double point : TRIG_POINTS) {
            if (Double.doubleToLongBits(point) == Double.doubleToLongBits(x)) {
                return true;
            }
        }
        return false;
    }

    private static Stream<Arguments> filteredCases(String resourcePath) {
        return CsvTestData.rows(resourcePath).stream()
                .filter(row -> containsPoint(row.x()))
                .map(row -> Arguments.of(row.x(), row.expected()));
    }
}
