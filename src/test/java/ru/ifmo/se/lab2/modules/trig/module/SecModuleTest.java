package ru.ifmo.se.lab2.modules.trig.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.support.spies.TrackingMathModuleSpy;

class SecModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnReciprocalOfCos() {
        TrackingMathModuleSpy cosSpy = new TrackingMathModuleSpy(x -> 0.5);
        SecModule secModule = new SecModule(cosSpy, EPS);

        assertEquals(2.0, secModule.calculate(1.0), EPS);
        assertEquals(1, cosSpy.getCallCount());
        assertEquals(1.0, cosSpy.getArguments().get(0), EPS);
    }

    @Test
    void shouldReturnNaNWhenCosIsZero() {
        TrackingMathModuleSpy cosSpy = new TrackingMathModuleSpy(x -> 0.0);
        SecModule secModule = new SecModule(cosSpy, EPS);

        assertTrue(Double.isNaN(secModule.calculate(1.0)));
        assertEquals(1, cosSpy.getCallCount());
    }
}
