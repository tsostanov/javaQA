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
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;

class CscModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnReciprocalOfSin() {
        MathModule sinModule = mock(MathModule.class);
        when(sinModule.calculate(1.0)).thenReturn(-0.25);
        CscModule cscModule = new CscModule(sinModule, EPS);

        assertEquals(-4.0, cscModule.calculate(1.0), EPS);
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(sinModule);
    }

    @Test
    void shouldReturnNaNWhenSinIsZero() {
        MathModule sinModule = mock(MathModule.class);
        when(sinModule.calculate(1.0)).thenReturn(0.0);
        CscModule cscModule = new CscModule(sinModule, EPS);

        assertTrue(Double.isNaN(cscModule.calculate(1.0)));
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(sinModule);
    }

    @Test
    void shouldReturnNaNWhenSinIsTooCloseToZero() {
        MathModule sinModule = mock(MathModule.class);
        when(sinModule.calculate(1.0)).thenReturn(1.0E-9);
        CscModule cscModule = new CscModule(sinModule, EPS);

        assertTrue(Double.isNaN(cscModule.calculate(1.0)));
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(sinModule);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.3, -1.2, -0.7, 0.7})
    void shouldBePeriodicByTwoPiWithRealSin(double x) {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);
        CscModule cscModule = new CscModule(sinModule, 1.0E-10);

        assertEquals(cscModule.calculate(x), cscModule.calculate(x + 2.0 * Math.PI), 1.0E-6);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.3, -1.2, -0.7, 0.7})
    void shouldStayCloseToMathCsc(double x) {
        SinModule sinModule = new SinModule(new CosModule(1.0E-10), 1.0E-10);
        CscModule cscModule = new CscModule(sinModule, 1.0E-10);

        assertEquals(1.0 / Math.sin(x), cscModule.calculate(x), 1.0E-6);
    }
}
