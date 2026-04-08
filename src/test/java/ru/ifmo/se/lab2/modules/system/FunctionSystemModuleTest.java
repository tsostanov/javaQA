package ru.ifmo.se.lab2.modules.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;

class FunctionSystemModuleTest {
    private static final double EPS = 1.0E-5;
    private FunctionSystemModule functionSystem;

    @BeforeEach
    void setUp() {
        double precision = 1.0E-10;
        CosModule cos = new CosModule(precision);
        SinModule sin = new SinModule(cos, precision);
        SecModule sec = new SecModule(cos, precision);
        CscModule csc = new CscModule(sin, precision);
        CotModule cot = new CotModule(cos, sin, precision);
        LnModule ln = new LnModule(precision);
        LogModule log2 = new LogModule(2.0, ln, precision);
        LogModule log3 = new LogModule(3.0, ln, precision);
        LogModule log5 = new LogModule(5.0, ln, precision);
        LogModule log10 = new LogModule(10.0, ln, precision);
        functionSystem = new FunctionSystemModule(cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, precision);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.3, -1.2, -0.7})
    void shouldCalculateTrigBranch(double x) {
        double sin = Math.sin(x);
        double cos = Math.cos(x);
        double sec = 1.0 / cos;
        double csc = 1.0 / sin;
        double cot = cos / sin;
        double numerator = Math.pow((((csc / csc) / sec) - sin + cot), 3.0) + (cos + csc) - (sec - sin);
        double denominator = ((Math.pow(cot, 2.0) / (csc + sec)) * Math.pow(sin, 2.0));
        double expected = Math.pow(numerator / denominator, 2.0) / cot;

        assertEquals(expected, functionSystem.calculate(x), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.7, 1.5, 3.0})
    void shouldCalculateLogBranch(double x) {
        double log10 = Math.log10(x);
        double log2 = Math.log(x) / Math.log(2.0);
        double log3 = Math.log(x) / Math.log(3.0);
        double log5 = Math.log(x) / Math.log(5.0);
        double ln = Math.log(x);
        double expected = (log10 * log10 * log2) + log3 + log5 + Math.pow(ln, 3.0);

        assertEquals(expected, functionSystem.calculate(x), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -Math.PI})
    void shouldReturnNaNAtSingularPointsOfSelectedBranch(double x) {
        assertTrue(Double.isNaN(functionSystem.calculate(x)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Math.PI, 2.5})
    void shouldUseLogBranchForPositiveValues(double x) {
        assertTrue(Double.isFinite(functionSystem.calculate(x)));
    }

    @ParameterizedTest
    @MethodSource("trigBranchNaNDependencies")
    void shouldReturnNaNWhenTrigDependencyReturnsNaN(
            MathModule sin,
            MathModule cos,
            MathModule sec,
            MathModule csc,
            MathModule cot
    ) {
        FunctionSystemModule system = new FunctionSystemModule(
                cos, sin, sec, csc, cot,
                constantModule(1.0), constantModule(1.0), constantModule(1.0), constantModule(1.0), constantModule(1.0),
                1.0E-8
        );

        assertTrue(Double.isNaN(system.calculate(-1.0)));
    }

    @ParameterizedTest
    @MethodSource("logBranchNaNDependencies")
    void shouldReturnNaNWhenLogDependencyReturnsNaN(
            MathModule ln,
            MathModule log2,
            MathModule log3,
            MathModule log5,
            MathModule log10
    ) {
        FunctionSystemModule system = new FunctionSystemModule(
                constantModule(0.5), constantModule(0.5), constantModule(2.0), constantModule(2.0), constantModule(1.0),
                ln, log2, log3, log5, log10,
                1.0E-8
        );

        assertTrue(Double.isNaN(system.calculate(1.0)));
    }

    private static Stream<Arguments> trigBranchNaNDependencies() {
        return Stream.of(
                Arguments.of(nanModule(), constantModule(0.5), constantModule(2.0), constantModule(2.0), constantModule(1.0)),
                Arguments.of(constantModule(0.5), nanModule(), constantModule(2.0), constantModule(2.0), constantModule(1.0)),
                Arguments.of(constantModule(0.5), constantModule(0.5), nanModule(), constantModule(2.0), constantModule(1.0)),
                Arguments.of(constantModule(0.5), constantModule(0.5), constantModule(2.0), nanModule(), constantModule(1.0)),
                Arguments.of(constantModule(0.5), constantModule(0.5), constantModule(2.0), constantModule(2.0), nanModule())
        );
    }

    private static Stream<Arguments> logBranchNaNDependencies() {
        return Stream.of(
                Arguments.of(nanModule(), constantModule(1.0), constantModule(1.0), constantModule(1.0), constantModule(1.0)),
                Arguments.of(constantModule(1.0), nanModule(), constantModule(1.0), constantModule(1.0), constantModule(1.0)),
                Arguments.of(constantModule(1.0), constantModule(1.0), nanModule(), constantModule(1.0), constantModule(1.0)),
                Arguments.of(constantModule(1.0), constantModule(1.0), constantModule(1.0), nanModule(), constantModule(1.0)),
                Arguments.of(constantModule(1.0), constantModule(1.0), constantModule(1.0), constantModule(1.0), nanModule())
        );
    }

    private static MathModule constantModule(double value) {
        return x -> value;
    }

    private static MathModule nanModule() {
        return x -> Double.NaN;
    }
}
