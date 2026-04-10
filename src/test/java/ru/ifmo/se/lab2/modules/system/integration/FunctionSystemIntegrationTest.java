package ru.ifmo.se.lab2.modules.system.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.stream.Stream;
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
}
