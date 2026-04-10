package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;
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
}
