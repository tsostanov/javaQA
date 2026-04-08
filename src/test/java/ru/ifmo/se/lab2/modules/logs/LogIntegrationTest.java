package ru.ifmo.se.lab2.modules.logs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.stubs.ModuleStubFactory;

class LogIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double[] POSITIVE_POINTS = {0.2, 0.7, 1.5, 2.5, 3.0, Math.PI};
    private final LnModule lnModule = new LnModule(PRECISION);

    @ParameterizedTest
    @ValueSource(doubles = {2.0, 3.0, 5.0, 10.0})
    void shouldIntegrateLogWithLnModule(double base) {
        LogModule logModule = new LogModule(base, lnModule, PRECISION);

        for (double x : POSITIVE_POINTS) {
            assertEquals(Math.log(x) / Math.log(base), logModule.calculate(x), EPS);
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.7, 1.5, 2.5, 3.0, Math.PI})
    void shouldIntegrateLogBranchIntoSystemWithTrigStubs(double x) {
        MathModule cosStub = ModuleStubFactory.cosStub(-2.3, -1.2, -0.7, -Math.PI, 0.0);
        MathModule sinStub = ModuleStubFactory.sinStub(-2.3, -1.2, -0.7, -Math.PI, 0.0);
        MathModule secStub = ModuleStubFactory.secStub(-2.3, -1.2, -0.7, -Math.PI, 0.0);
        MathModule cscStub = ModuleStubFactory.cscStub(-2.3, -1.2, -0.7, -Math.PI);
        MathModule cotStub = ModuleStubFactory.cotStub(-2.3, -1.2, -0.7, -Math.PI);
        LogModule log2 = new LogModule(2.0, lnModule, PRECISION);
        LogModule log3 = new LogModule(3.0, lnModule, PRECISION);
        LogModule log5 = new LogModule(5.0, lnModule, PRECISION);
        LogModule log10 = new LogModule(10.0, lnModule, PRECISION);
        FunctionSystemModule system = new FunctionSystemModule(
                cosStub, sinStub, secStub, cscStub, cotStub, lnModule, log2, log3, log5, log10, PRECISION
        );

        assertEquals(expectedLogBranch(x), system.calculate(x), 1.0E-5);
    }

    private static double expectedLogBranch(double x) {
        double ln = Math.log(x);
        double log2 = Math.log(x) / Math.log(2.0);
        double log3 = Math.log(x) / Math.log(3.0);
        double log5 = Math.log(x) / Math.log(5.0);
        double log10 = Math.log10(x);
        return (log10 * log10 * log2) + log3 + log5 + Math.pow(ln, 3.0);
    }
}
