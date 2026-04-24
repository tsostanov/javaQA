package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;

class TrigBottomUpIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double DELTA = 1.0E-6;
    private static final double LARGE_VALUE_THRESHOLD = 1.0E5;

    @ParameterizedTest
    @MethodSource("secSingularPoints")
    void shouldReturnNaNForSecAtSingularPoints(double x) {
        CosModule cos = new CosModule(PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);

        assertTrue(Double.isNaN(sec.calculate(x)));
    }

    @ParameterizedTest
    @MethodSource("cscSingularPoints")
    void shouldReturnNaNForCscAtSingularPoints(double x) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);

        assertTrue(Double.isNaN(csc.calculate(x)));
    }

    @ParameterizedTest
    @MethodSource("cotSingularPoints")
    void shouldReturnNaNForCotAtSingularPoints(double x) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);

        assertTrue(Double.isNaN(cot.calculate(x)));
    }

    @ParameterizedTest
    @MethodSource("secNearSingularPoints")
    void shouldGrowLargeNearSecSingularPoints(double x, double expectedSign) {
        CosModule cos = new CosModule(PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);

        assertLargeMagnitudeWithSign(sec.calculate(x), expectedSign);
    }

    @ParameterizedTest
    @MethodSource("cscNearSingularPoints")
    void shouldGrowLargeNearCscSingularPoints(double x, double expectedSign) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);

        assertLargeMagnitudeWithSign(csc.calculate(x), expectedSign);
    }

    @ParameterizedTest
    @MethodSource("cotNearSingularPoints")
    void shouldGrowLargeNearCotSingularPoints(double x, double expectedSign) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);

        assertLargeMagnitudeWithSign(cot.calculate(x), expectedSign);
    }

    @ParameterizedTest
    @MethodSource("sinCases")
    void shouldBuildSinOnTopOfRealCos(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);

        assertEquals(expected, sin.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("secCases")
    void shouldBuildSecOnTopOfRealCos(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);

        assertEquals(expected, sec.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("cscCases")
    void shouldBuildCscOnTopOfRealSin(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);

        assertEquals(expected, csc.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("cotCases")
    void shouldBuildCotOnTopOfRealSinAndCos(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);

        assertEquals(expected, cot.calculate(x), EPS);
    }

    private static Stream<Arguments> sinCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/sin-stub.csv");
    }

    private static Stream<Arguments> secCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/sec-stub.csv");
    }

    private static Stream<Arguments> cscCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/csc-stub.csv");
    }

    private static Stream<Arguments> cotCases() {
        return TrigIntegrationSupport.cases("/testdata/integration/trig/cot-stub.csv");
    }

    private static Stream<Arguments> secSingularPoints() {
        return Stream.of(
                Arguments.of(-1.5707963267948966),
                Arguments.of(1.5707963267948966)
        );
    }

    private static Stream<Arguments> cscSingularPoints() {
        return Stream.of(
                Arguments.of(-3.1415926535897931),
                Arguments.of(0.0)
        );
    }

    private static Stream<Arguments> cotSingularPoints() {
        return Stream.of(
                Arguments.of(-3.1415926535897931),
                Arguments.of(0.0)
        );
    }

    private static Stream<Arguments> secNearSingularPoints() {
        return Stream.of(
                Arguments.of(-Math.PI / 2.0 - DELTA, -1.0),
                Arguments.of(-Math.PI / 2.0 + DELTA, 1.0),
                Arguments.of(Math.PI / 2.0 - DELTA, 1.0),
                Arguments.of(Math.PI / 2.0 + DELTA, -1.0)
        );
    }

    private static Stream<Arguments> cscNearSingularPoints() {
        return Stream.of(
                Arguments.of(-DELTA, -1.0),
                Arguments.of(DELTA, 1.0),
                Arguments.of(Math.PI - DELTA, 1.0),
                Arguments.of(Math.PI + DELTA, -1.0)
        );
    }

    private static Stream<Arguments> cotNearSingularPoints() {
        return Stream.of(
                Arguments.of(-DELTA, -1.0),
                Arguments.of(DELTA, 1.0),
                Arguments.of(Math.PI - DELTA, -1.0),
                Arguments.of(Math.PI + DELTA, 1.0)
        );
    }

    private static void assertLargeMagnitudeWithSign(double actual, double expectedSign) {
        assertTrue(Double.isFinite(actual));
        assertTrue(Math.abs(actual) > LARGE_VALUE_THRESHOLD);
        assertTrue(actual * expectedSign > 0.0);
    }
}
