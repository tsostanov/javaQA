package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;

class CscModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnReciprocalOfSin() {
        MathModule sinStub = x -> -0.25;
        CscModule cscModule = new CscModule(sinStub, EPS);

        assertEquals(-4.0, cscModule.calculate(1.0), EPS);
    }

    @Test
    void shouldReturnNaNWhenSinIsZero() {
        MathModule sinStub = x -> 0.0;
        CscModule cscModule = new CscModule(sinStub, EPS);

        assertTrue(Double.isNaN(cscModule.calculate(1.0)));
    }
}
