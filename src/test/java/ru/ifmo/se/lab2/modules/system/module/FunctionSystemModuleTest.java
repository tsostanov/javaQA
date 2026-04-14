package ru.ifmo.se.lab2.modules.system.module;

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
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.support.CsvTestData;
import ru.ifmo.se.lab2.support.MockitoMathModuleFactory;

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
    @MethodSource("trigBranchCases")
    void shouldCalculateTrigBranch(double x, double expected) {
        assertEquals(expected, functionSystem.calculate(x), EPS);
    }

    @ParameterizedTest
    @MethodSource("logBranchCases")
    void shouldCalculateLogBranch(double x, double expected) {
        assertEquals(expected, functionSystem.calculate(x), EPS);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.5707963267948966, -3.1415926535897931, -4.71238898038469})
    void shouldReturnNaNAtSingularPointsOfSelectedBranch(double x) {
        assertTrue(Double.isNaN(functionSystem.calculate(x)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {3.1415926535897931, 2.5})
    void shouldUseLogBranchForPositiveValues(double x) {
        assertTrue(Double.isFinite(functionSystem.calculate(x)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void shouldReturnNaNForNonFiniteInput(double x) {
        assertTrue(Double.isNaN(functionSystem.calculate(x)));
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
        return MockitoMathModuleFactory.constant(value);
    }

    private static MathModule nanModule() {
        return MockitoMathModuleFactory.nan();
    }

    private static Stream<Arguments> trigBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/trig-branch.csv");
    }

    private static Stream<Arguments> logBranchCases() {
        return CsvTestData.arguments("/testdata/module/system/log-branch.csv");
    }
}
