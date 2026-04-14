package ru.ifmo.se.lab2.modules.system.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
