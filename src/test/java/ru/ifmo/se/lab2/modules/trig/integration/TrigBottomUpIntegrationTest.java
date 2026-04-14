package ru.ifmo.se.lab2.modules.trig.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
