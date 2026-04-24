package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;

class SecModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnReciprocalOfCos() {
        MathModule cosModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(0.5);
        SecModule secModule = new SecModule(cosModule, EPS);

        assertEquals(2.0, secModule.calculate(1.0), EPS);
        verify(cosModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule);
    }

    @Test
    void shouldReturnNaNWhenCosIsZero() {
        MathModule cosModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(0.0);
        SecModule secModule = new SecModule(cosModule, EPS);

        assertTrue(Double.isNaN(secModule.calculate(1.0)));
        verify(cosModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule);
    }

    @Test
    void shouldReturnNaNWhenCosIsTooCloseToZero() {
        MathModule cosModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(1.0E-9);
        SecModule secModule = new SecModule(cosModule, EPS);

        assertTrue(Double.isNaN(secModule.calculate(1.0)));
        verify(cosModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.2, -0.7, 0.7})
    void shouldBePeriodicByTwoPiWithRealCos(double x) {
        SecModule secModule = new SecModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(secModule.calculate(x), secModule.calculate(x + 2.0 * Math.PI), 1.0E-6);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.2, -0.7, 0.7})
    void shouldStayCloseToMathSec(double x) {
        SecModule secModule = new SecModule(new CosModule(1.0E-10), 1.0E-10);

        assertEquals(1.0 / Math.cos(x), secModule.calculate(x), 1.0E-6);
    }
}
