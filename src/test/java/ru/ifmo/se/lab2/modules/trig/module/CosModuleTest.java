package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.support.CsvTestData;

class CosModuleTest {
    private static final double EPS = 1.0E-6;
    private static final String COS_VALUES = "/testdata/module/trig/cos.csv";
    private final CosModule cosModule = new CosModule(1.0E-10);

    @ParameterizedTest
    @MethodSource("cosCases")
    void shouldApproximateTabulatedCos(double x, double expected) {
        assertEquals(expected, cosModule.calculate(x), EPS);
    }

    @Test
    void shouldNormalizeAnglesBelowNegativePi() {
        double x = -4.71238898038469;

        assertEquals(CsvTestData.expected(COS_VALUES, x), cosModule.calculate(x), EPS);
    }

    @Test
    void shouldReturnNaNForNaNInput() {
        assertTrue(Double.isNaN(cosModule.calculate(Double.NaN)));
    }

    @Test
    void shouldReturnNaNForInfiniteInput() {
        assertTrue(Double.isNaN(cosModule.calculate(Double.POSITIVE_INFINITY)));
    }

    private static Stream<Arguments> cosCases() {
        return CsvTestData.arguments(COS_VALUES);
    }
}
