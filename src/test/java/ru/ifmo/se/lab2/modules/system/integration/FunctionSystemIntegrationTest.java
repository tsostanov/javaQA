package ru.ifmo.se.lab2.modules.system.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import ru.ifmo.se.lab2.support.stubs.ModuleStubFactory;

class FunctionSystemIntegrationTest {
    private static final double PRECISION = 1.0E-10;
    private static final double PI = 3.1415926535897931;
    private final LnModule lnModule = new LnModule(PRECISION);

    @ParameterizedTest
    @MethodSource("trigBranchCases")
    void shouldIntegrateTrigBranchIntoSystemWithLogStubs(double x, double expected) {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);
        MathModule lnStub = ModuleStubFactory.lnStub(0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log2Stub = ModuleStubFactory.logStub(2.0, 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log3Stub = ModuleStubFactory.logStub(3.0, 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log5Stub = ModuleStubFactory.logStub(5.0, 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        MathModule log10Stub = ModuleStubFactory.logStub(10.0, 0.2, 0.7, 1.5, 2.5, 3.0, PI);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, lnStub, log2Stub, log3Stub, log5Stub, log10Stub, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
    }

    @ParameterizedTest
    @MethodSource("logBranchCases")
    void shouldIntegrateLogBranchIntoSystemWithTrigStubs(double x, double expected) {
        MathModule cosStub = ModuleStubFactory.cosStub(-2.3, -1.2, -0.7, -PI, 0.0);
        MathModule sinStub = ModuleStubFactory.sinStub(-2.3, -1.2, -0.7, -PI, 0.0);
        MathModule secStub = ModuleStubFactory.secStub(-2.3, -1.2, -0.7, -PI);
        MathModule cscStub = ModuleStubFactory.cscStub(-2.3, -1.2, -0.7, -PI);
        MathModule cotStub = ModuleStubFactory.cotStub(-2.3, -1.2, -0.7, -PI);
        LogModule log2 = new LogModule(2.0, lnModule, PRECISION);
        LogModule log3 = new LogModule(3.0, lnModule, PRECISION);
        LogModule log5 = new LogModule(5.0, lnModule, PRECISION);
        LogModule log10 = new LogModule(10.0, lnModule, PRECISION);
        FunctionSystemModule system = new FunctionSystemModule(
                cosStub, sinStub, secStub, cscStub, cotStub, lnModule, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expected, system.calculate(x), 1.0E-5);
    }

    private static Stream<Arguments> trigBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/trig-branch.csv");
    }

    private static Stream<Arguments> logBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/log-branch.csv");
    }
}
