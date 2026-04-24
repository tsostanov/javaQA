package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;

class SinModuleTest {
    private static final double HALF_PI = 1.5707963267948966;

    @Test
    void shouldBeDerivedFromCosModule() {
        double shiftedArgument = HALF_PI - 1.23;
        MathModule cosMock = mock(MathModule.class);
        when(cosMock.calculate(shiftedArgument)).thenReturn(7.5);
        SinModule sinModule = new SinModule(cosMock, 1.0E-8);

        assertEquals(7.5, sinModule.calculate(1.23), 1.0E-12);
        verify(cosMock).calculate(shiftedArgument);
        verifyNoMoreInteractions(cosMock);
    }

    @Test
    void shouldReturnNaNForNaNInput() {
        SinModule sinModule = new SinModule(new CosModule(1.0E-8), 1.0E-8);

        assertTrue(Double.isNaN(sinModule.calculate(Double.NaN)));
    }

    @Test
    void shouldReturnNaNForInfiniteInput() {
        SinModule sinModule = new SinModule(new CosModule(1.0E-8), 1.0E-8);

        assertTrue(Double.isNaN(sinModule.calculate(Double.POSITIVE_INFINITY)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.3, -1.2, -0.7, 0.7})
    void shouldBePeriodicByTwoPi(double x) {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(sinModule.calculate(x), sinModule.calculate(x + 2.0 * Math.PI), 1.0E-6);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.3, -1.2, -0.7, 0.0, 0.7})
    void shouldStayCloseToMathSin(double x) {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(Math.sin(x), sinModule.calculate(x), 1.0E-6);
    }

    @Test
    void shouldReachMaximumAtHalfPi() {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(1.0, sinModule.calculate(Math.PI / 2.0), 1.0E-6);
    }

    @Test
    void shouldReachMinimumAtNegativeHalfPi() {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(-1.0, sinModule.calculate(-Math.PI / 2.0), 1.0E-6);
    }

    @ParameterizedTest
    @MethodSource("sinEquivalenceClassCases")
    void shouldMatchExpectedSignForSinEquivalenceClass(double x, double expectedSign) {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);
        double actual = sinModule.calculate(x);

        assertTrue(actual * expectedSign > 0.0);
    }

    private static Stream<Arguments> sinEquivalenceClassCases() {
        return Stream.of(
                Arguments.of(Math.PI / 6.0, 1.0),
                Arguments.of(2.0 * Math.PI / 3.0, 1.0),
                Arguments.of(-2.0 * Math.PI / 3.0, -1.0),
                Arguments.of(-Math.PI / 6.0, -1.0)
        );
    }
}
