package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;

class CotModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnCosDividedBySin() {
        MathModule cosModule = mock(MathModule.class);
        MathModule sinModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(0.75);
        when(sinModule.calculate(1.0)).thenReturn(-0.25);
        CotModule cotModule = new CotModule(cosModule, sinModule, EPS);

        assertEquals(-3.0, cotModule.calculate(1.0), EPS);
        verify(cosModule).calculate(1.0);
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule, sinModule);
    }

    @Test
    void shouldReturnNaNWhenSinIsZero() {
        MathModule cosModule = mock(MathModule.class);
        MathModule sinModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(1.0);
        when(sinModule.calculate(1.0)).thenReturn(0.0);
        CotModule cotModule = new CotModule(cosModule, sinModule, EPS);

        assertTrue(Double.isNaN(cotModule.calculate(1.0)));
        verify(cosModule).calculate(1.0);
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule, sinModule);
    }

    @Test
    void shouldReturnNaNWhenSinIsTooCloseToZero() {
        MathModule cosModule = mock(MathModule.class);
        MathModule sinModule = mock(MathModule.class);
        when(cosModule.calculate(1.0)).thenReturn(1.0);
        when(sinModule.calculate(1.0)).thenReturn(1.0E-9);
        CotModule cotModule = new CotModule(cosModule, sinModule, EPS);

        assertTrue(Double.isNaN(cotModule.calculate(1.0)));
        verify(cosModule).calculate(1.0);
        verify(sinModule).calculate(1.0);
        verifyNoMoreInteractions(cosModule, sinModule);
    }
}
