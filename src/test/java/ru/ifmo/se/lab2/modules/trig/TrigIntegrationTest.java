package ru.ifmo.se.lab2.modules.trig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.stubs.ModuleStubFactory;

class TrigIntegrationTest {
    private static final double EPS = 1.0E-6;
    private static final double PRECISION = 1.0E-10;
    private static final double[] TRIG_POINTS = {-2.3, -1.2, -0.7};

    @Test
    void shouldIntegrateSinWithCosStub() {
        double[] cosArguments = transformedArguments(TRIG_POINTS);
        MathModule cosStub = ModuleStubFactory.cosStub(cosArguments);
        SinModule sinModule = new SinModule(cosStub, PRECISION);

        for (double x : TRIG_POINTS) {
            assertEquals(Math.sin(x), sinModule.calculate(x), EPS);
        }
    }

    @Test
    void shouldIntegrateSecWithCosStub() {
        MathModule cosStub = ModuleStubFactory.cosStub(TRIG_POINTS);
        SecModule secModule = new SecModule(cosStub, PRECISION);

        for (double x : TRIG_POINTS) {
            assertEquals(1.0 / Math.cos(x), secModule.calculate(x), EPS);
        }
    }

    @Test
    void shouldIntegrateCscWithSinStub() {
        MathModule sinStub = ModuleStubFactory.sinStub(TRIG_POINTS);
        CscModule cscModule = new CscModule(sinStub, PRECISION);

        for (double x : TRIG_POINTS) {
            assertEquals(1.0 / Math.sin(x), cscModule.calculate(x), EPS);
        }
    }

    @Test
    void shouldIntegrateCotWithSinAndCosStubs() {
        MathModule cosStub = ModuleStubFactory.cosStub(TRIG_POINTS);
        MathModule sinStub = ModuleStubFactory.sinStub(TRIG_POINTS);
        CotModule cotModule = new CotModule(cosStub, sinStub, PRECISION);

        for (double x : TRIG_POINTS) {
            assertEquals(Math.cos(x) / Math.sin(x), cotModule.calculate(x), EPS);
        }
    }

    @Test
    void shouldIntegrateTrigBranchIntoSystemWithLogStubs() {
        CosModule cos = new CosModule(PRECISION);
        SinModule sin = new SinModule(cos, PRECISION);
        SecModule sec = new SecModule(cos, PRECISION);
        CscModule csc = new CscModule(sin, PRECISION);
        CotModule cot = new CotModule(cos, sin, PRECISION);
        MathModule lnStub = ModuleStubFactory.lnStub(0.2, 0.7, 1.5, 2.5, 3.0, Math.PI);
        MathModule log2Stub = ModuleStubFactory.logStub(2.0, 0.2, 0.7, 1.5, 2.5, 3.0, Math.PI);
        MathModule log3Stub = ModuleStubFactory.logStub(3.0, 0.2, 0.7, 1.5, 2.5, 3.0, Math.PI);
        MathModule log5Stub = ModuleStubFactory.logStub(5.0, 0.2, 0.7, 1.5, 2.5, 3.0, Math.PI);
        MathModule log10Stub = ModuleStubFactory.logStub(10.0, 0.2, 0.7, 1.5, 2.5, 3.0, Math.PI);
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot, lnStub, log2Stub, log3Stub, log5Stub, log10Stub, PRECISION
        );

        for (double x : TRIG_POINTS) {
            assertEquals(expectedTrigBranch(x), system.calculate(x), 1.0E-5);
        }
    }

    private static double expectedTrigBranch(double x) {
        double sin = Math.sin(x);
        double cos = Math.cos(x);
        double sec = 1.0 / cos;
        double csc = 1.0 / sin;
        double cot = cos / sin;
        double numerator = Math.pow((((csc / csc) / sec) - sin + cot), 3.0) + (cos + csc) - (sec - sin);
        double denominator = (Math.pow(cot, 2.0) / (csc + sec)) * Math.pow(sin, 2.0);
        return Math.pow(numerator / denominator, 2.0) / cot;
    }

    private static double[] transformedArguments(double[] points) {
        double[] result = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            result[i] = Math.PI / 2.0 - points[i];
        }
        return result;
    }
}
