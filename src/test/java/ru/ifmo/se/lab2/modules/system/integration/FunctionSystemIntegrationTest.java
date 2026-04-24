package ru.ifmo.se.lab2.modules.system.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

class FunctionSystemIntegrationTest {
    private static final double PRECISION = 1.0E-10;
    private static final double PI = 3.1415926535897931;

    @ParameterizedTest
    @MethodSource("trigFromMockedCosCases")
    void shouldBuildTrigBranchBottomUpFromMockedCos(
            double x,
            double cosAtX,
            double sinAtX,
            double expected
    ) {
        MathModule cos = mock(MathModule.class);
        double shifted = Math.PI / 2.0 - x;
        when(cos.calculate(x)).thenReturn(cosAtX);
        when(cos.calculate(shifted)).thenReturn(sinAtX);

        SinModule sin = new SinModule(cos, PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
        verify(cos, atLeastOnce()).calculate(x);
        verify(cos, atLeastOnce()).calculate(shifted);
        verifyNoInteractions(ln, log2, log3, log5, log10);
    }

    @ParameterizedTest
    @MethodSource("logFromMockedLnCases")
    void shouldBuildLogBranchBottomUpFromMockedLn(double x, double lnAtX, double expected) {
        MathModule ln = mock(MathModule.class);
        when(ln.calculate(x)).thenReturn(lnAtX);
        when(ln.calculate(2.0)).thenReturn(CsvTestData.expected("/testdata/module/logs/ln.csv", 2.0));
        when(ln.calculate(3.0)).thenReturn(CsvTestData.expected("/testdata/module/logs/ln.csv", 3.0));
        when(ln.calculate(5.0)).thenReturn(CsvTestData.expected("/testdata/module/logs/ln.csv", 5.0));
        when(ln.calculate(10.0)).thenReturn(CsvTestData.expected("/testdata/module/logs/ln.csv", 10.0));

        LogModule log2 = new LogModule(2.0, ln, PRECISION);
        LogModule log3 = new LogModule(3.0, ln, PRECISION);
        LogModule log5 = new LogModule(5.0, ln, PRECISION);
        LogModule log10 = new LogModule(10.0, ln, PRECISION);
        MathModule cos = mock(MathModule.class);
        MathModule sin = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
        verify(ln, atLeastOnce()).calculate(x);
        verifyNoInteractions(cos, sin, sec, csc, cot);
    }

    @Test
    void shouldUseTrigBranchAtZeroWithMockedDependencies() {
        MathModule sin = mock(MathModule.class);
        MathModule cos = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);
        when(sin.calculate(0.0)).thenReturn(1.0);
        when(cos.calculate(0.0)).thenReturn(1.0);
        when(sec.calculate(0.0)).thenReturn(1.0);
        when(csc.calculate(0.0)).thenReturn(1.0);
        when(cot.calculate(0.0)).thenReturn(1.0);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(36.0, system.calculate(0.0), 1.0E-12);
        verify(sin).calculate(0.0);
        verify(cos).calculate(0.0);
        verify(sec).calculate(0.0);
        verify(csc).calculate(0.0);
        verify(cot).calculate(0.0);
        verifyNoInteractions(ln, log2, log3, log5, log10);
    }

    @Test
    void shouldUseLogBranchForPositiveValueNearZeroWithMockedDependencies() {
        double x = 1.0E-8;
        MathModule cos = mock(MathModule.class);
        MathModule sin = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);
        when(ln.calculate(x)).thenReturn(1.0);
        when(log2.calculate(x)).thenReturn(2.0);
        when(log3.calculate(x)).thenReturn(3.0);
        when(log5.calculate(x)).thenReturn(5.0);
        when(log10.calculate(x)).thenReturn(10.0);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(209.0, system.calculate(x), 1.0E-12);
        verify(ln).calculate(x);
        verify(log2).calculate(x);
        verify(log3).calculate(x);
        verify(log5).calculate(x);
        verify(log10).calculate(x);
        verifyNoInteractions(cos, sin, sec, csc, cot);
    }

    @ParameterizedTest
    @MethodSource("topDownTrigCases")
    void shouldComposeTrigBranchFromMockedDependencies(
            double x,
            double sinValue,
            double cosValue,
            double secValue,
            double cscValue,
            double cotValue
    ) {
        MathModule sin = mock(MathModule.class);
        MathModule cos = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);
        when(sin.calculate(x)).thenReturn(sinValue);
        when(cos.calculate(x)).thenReturn(cosValue);
        when(sec.calculate(x)).thenReturn(secValue);
        when(csc.calculate(x)).thenReturn(cscValue);
        when(cot.calculate(x)).thenReturn(cotValue);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expectedTrigBranch(sinValue, cosValue, secValue, cscValue, cotValue), system.calculate(x), 1.0E-12);
        verify(sin).calculate(x);
        verify(cos).calculate(x);
        verify(sec).calculate(x);
        verify(csc).calculate(x);
        verify(cot).calculate(x);
        verifyNoInteractions(ln, log2, log3, log5, log10);
    }

    @ParameterizedTest
    @MethodSource("topDownLogCases")
    void shouldComposeLogBranchFromMockedDependencies(
            double x,
            double lnValue,
            double log2Value,
            double log3Value,
            double log5Value,
            double log10Value
    ) {
        MathModule cos = mock(MathModule.class);
        MathModule sin = mock(MathModule.class);
        MathModule sec = mock(MathModule.class);
        MathModule csc = mock(MathModule.class);
        MathModule cot = mock(MathModule.class);
        MathModule ln = mock(MathModule.class);
        MathModule log2 = mock(MathModule.class);
        MathModule log3 = mock(MathModule.class);
        MathModule log5 = mock(MathModule.class);
        MathModule log10 = mock(MathModule.class);
        when(ln.calculate(x)).thenReturn(lnValue);
        when(log2.calculate(x)).thenReturn(log2Value);
        when(log3.calculate(x)).thenReturn(log3Value);
        when(log5.calculate(x)).thenReturn(log5Value);
        when(log10.calculate(x)).thenReturn(log10Value);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expectedLogBranch(lnValue, log2Value, log3Value, log5Value, log10Value), system.calculate(x), 1.0E-12);
        verify(ln).calculate(x);
        verify(log2).calculate(x);
        verify(log3).calculate(x);
        verify(log5).calculate(x);
        verify(log10).calculate(x);
        verifyNoInteractions(cos, sin, sec, csc, cot);
    }

    @ParameterizedTest
    @MethodSource("trigBranchCases")
    void shouldIntegrateTrigBranchIntoSystemWithLogStubs(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);
        MathModule lnStub = MockitoMathModuleFactory.tabulated("/testdata/module/logs/ln.csv", 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log2Stub = MockitoMathModuleFactory.tabulated("/testdata/module/logs/log2.csv", 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log3Stub = MockitoMathModuleFactory.tabulated("/testdata/module/logs/log3.csv", 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log5Stub = MockitoMathModuleFactory.tabulated("/testdata/module/logs/log5.csv", 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log10Stub = MockitoMathModuleFactory.tabulated("/testdata/module/logs/log10.csv", 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, lnStub, log2Stub, log3Stub, log5Stub, log10Stub, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
        verifyNoInteractions(lnStub, log2Stub, log3Stub, log5Stub, log10Stub);
    }

    @ParameterizedTest
    @MethodSource("logBranchCases")
    void shouldIntegrateLogBranchIntoSystemWithTrigStubs(double x, double expected) {
        MathModule cosStub = MockitoMathModuleFactory.constant(0.0);
        MathModule sinStub = MockitoMathModuleFactory.constant(0.0);
        MathModule secStub = MockitoMathModuleFactory.constant(0.0);
        MathModule cscStub = MockitoMathModuleFactory.constant(0.0);
        MathModule cotStub = MockitoMathModuleFactory.constant(0.0);
        LnModule lnModule = new LnModule(PRECISION);
        LogModule log2 = new LogModule(2.0, lnModule, PRECISION);
        LogModule log3 = new LogModule(3.0, lnModule, PRECISION);
        LogModule log5 = new LogModule(5.0, lnModule, PRECISION);
        LogModule log10 = new LogModule(10.0, lnModule, PRECISION);
        FunctionSystemModule system = new FunctionSystemModule(
                cosStub, sinStub, secStub, cscStub, cotStub, lnModule, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
        verifyNoInteractions(cosStub, sinStub, secStub, cscStub, cotStub);
    }

    private static Stream<Arguments> trigBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/trig-branch.csv");
    }

    private static Stream<Arguments> logBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/log-branch.csv");
    }

    private static Stream<Arguments> topDownTrigCases() {
        return Stream.of(
                Arguments.of(-0.5, 1.0, 1.0, 1.0, 1.0, 1.0),
                Arguments.of(-1.0, 2.0, 3.0, 0.5, 4.0, 5.0),
                Arguments.of(-2.0, 0.5, -1.5, 2.0, 3.0, -0.75)
        );
    }

    private static Stream<Arguments> topDownLogCases() {
        return Stream.of(
                Arguments.of(0.1, 1.0, 2.0, 3.0, 5.0, 10.0),
                Arguments.of(0.7, -0.5, 0.25, 1.5, -2.0, 0.75),
                Arguments.of(2.5, 2.0, -1.0, 0.5, 3.0, -4.0)
        );
    }

    private static Stream<Arguments> trigFromMockedCosCases() {
        return Stream.of(
                trigFromMockedCosCase(-2.3),
                trigFromMockedCosCase(-0.7)
        );
    }

    private static Arguments trigFromMockedCosCase(double x) {
        return Arguments.of(
                x,
                CsvTestData.expected("/testdata/integration/trig/cos-stub.csv", x),
                CsvTestData.expected("/testdata/integration/trig/sin-stub.csv", x),
                CsvTestData.expected("/testdata/module/system/trig-branch.csv", x)
        );
    }

    private static Stream<Arguments> logFromMockedLnCases() {
        return Stream.of(
                logFromMockedLnCase(0.2),
                logFromMockedLnCase(0.7),
                logFromMockedLnCase(1.5)
        );
    }

    private static Arguments logFromMockedLnCase(double x) {
        return Arguments.of(
                x,
                CsvTestData.expected("/testdata/module/logs/ln.csv", x),
                CsvTestData.expected("/testdata/module/system/log-branch.csv", x)
        );
    }

    private static double expectedTrigBranch(double sin, double cos, double sec, double csc, double cot) {
        double numerator = Math.pow((((safeDivide(csc, csc) / sec) - sin) + cot), 3.0) + (cos + csc) - (sec - sin);
        double denominator = safeDivide(Math.pow(cot, 2.0), csc + sec) * Math.pow(sin, 2.0);
        return safeDivide(Math.pow(safeDivide(numerator, denominator), 2.0), cot);
    }

    private static double expectedLogBranch(double ln, double log2, double log3, double log5, double log10) {
        return (log10 * log10 * log2) + log3 + log5 + Math.pow(ln, 3.0);
    }

    private static double safeDivide(double numerator, double denominator) {
        if (Double.isNaN(numerator) || Double.isNaN(denominator) || Math.abs(denominator) <= PRECISION) {
            return Double.NaN;
        }
        return numerator / denominator;
    }
}
