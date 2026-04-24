package ru.ifmo.se.lab2.modules.system.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class FunctionSystemRealIntegrationTest {
    private static final double EPS = 1.0E-5;
    private static final double PRECISION = 1.0E-10;

    @ParameterizedTest
    @MethodSource("systemTrigMathCases")
    void shouldMatchMathTrigBranchOnRepresentativePoints(double x) {
        FunctionSystemModule system = createRealSystem();
        double expected = expectedTrigBranchFromMath(x);
        double actual = system.calculate(x);

        assertEquals(expected, actual, Math.max(1.0E-5, Math.abs(expected) * 1.0E-6));
    }

    @ParameterizedTest
    @MethodSource("systemLogMathCases")
    void shouldMatchMathLogBranchOnRepresentativePoints(double x) {
        FunctionSystemModule system = createRealSystem();

        assertEquals(expectedLogBranchFromMath(x), system.calculate(x), 1.0E-5);
    }

    @ParameterizedTest
    @MethodSource("systemSingularPoints")
    void shouldReturnNaNAtRealSingularPoints(double x) {
        FunctionSystemModule system = createRealSystem();

        assertTrue(Double.isNaN(system.calculate(x)));
    }

    @ParameterizedTest
    @MethodSource("positiveNearZeroCases")
    void shouldUseFiniteLogBranchJustAboveZero(double x) {
        FunctionSystemModule system = createRealSystem();
        double actual = system.calculate(x);

        assertTrue(Double.isFinite(actual));
        assertEquals(expectedLogBranchFromMath(x), actual, 1.0E-5);
    }

    @ParameterizedTest
    @MethodSource("trigBranchCases")
    void shouldBuildSystemTrigBranchFromRealModules(double x, double expected) {
        FunctionSystemModule system = createRealSystem();

        assertEquals(expected, system.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("logBranchCases")
    void shouldBuildSystemLogBranchFromRealModules(double x, double expected) {
        FunctionSystemModule system = createRealSystem();

        assertEquals(expected, system.calculate(x), EPS);
    }

    private static FunctionSystemModule createRealSystem() {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);
        LnModule ln = new LnModule(PRECISION);
        LogModule log2 = new LogModule(2.0, ln, PRECISION);
        LogModule log3 = new LogModule(3.0, ln, PRECISION);
        LogModule log5 = new LogModule(5.0, ln, PRECISION);
        LogModule log10 = new LogModule(10.0, ln, PRECISION);
        return new FunctionSystemModule(cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION);
    }

    private static Stream<Arguments> trigBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/trig-branch.csv");
    }

    private static Stream<Arguments> logBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/log-branch.csv");
    }

    private static Stream<Arguments> systemTrigMathCases() {
        return Stream.of(
                Arguments.of(-2.3),
                Arguments.of(-1.2),
                Arguments.of(-0.7)
        );
    }

    private static Stream<Arguments> systemLogMathCases() {
        return Stream.of(
                Arguments.of(0.2),
                Arguments.of(0.7),
                Arguments.of(1.5),
                Arguments.of(2.5)
        );
    }

    private static Stream<Arguments> systemSingularPoints() {
        return Stream.of(
                Arguments.of(0.0),
                Arguments.of(-Math.PI / 2.0),
                Arguments.of(-Math.PI)
        );
    }

    private static Stream<Arguments> positiveNearZeroCases() {
        return Stream.of(
                Arguments.of(1.0E-6),
                Arguments.of(1.0E-4)
        );
    }

    private static double expectedTrigBranchFromMath(double x) {
        double sin = Math.sin(x);
        double cos = Math.cos(x);
        double sec = safeDivide(1.0, cos);
        double csc = safeDivide(1.0, sin);
        double cot = safeDivide(cos, sin);
        double numerator = Math.pow((((safeDivide(csc, csc) / sec) - sin) + cot), 3.0) + (cos + csc) - (sec - sin);
        double denominator = safeDivide(Math.pow(cot, 2.0), csc + sec) * Math.pow(sin, 2.0);
        return safeDivide(Math.pow(safeDivide(numerator, denominator), 2.0), cot);
    }

    private static double expectedLogBranchFromMath(double x) {
        double ln = Math.log(x);
        double log2 = ln / Math.log(2.0);
        double log3 = ln / Math.log(3.0);
        double log5 = ln / Math.log(5.0);
        double log10 = ln / Math.log(10.0);
        return (log10 * log10 * log2) + log3 + log5 + Math.pow(ln, 3.0);
    }

    private static double safeDivide(double numerator, double denominator) {
        if (Double.isNaN(numerator) || Double.isNaN(denominator) || Math.abs(denominator) <= PRECISION) {
            return Double.NaN;
        }
        return numerator / denominator;
    }
}
