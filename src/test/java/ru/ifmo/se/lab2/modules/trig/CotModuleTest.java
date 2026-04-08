package ru.ifmo.se.lab2.modules.trig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.spies.TrackingMathModuleSpy;

class CotModuleTest {
    private static final double EPS = 1.0E-8;

    @Test
    void shouldReturnCosDividedBySin() {
        TrackingMathModuleSpy cosSpy = new TrackingMathModuleSpy(x -> 0.75);
        TrackingMathModuleSpy sinSpy = new TrackingMathModuleSpy(x -> -0.25);
        CotModule cotModule = new CotModule(cosSpy, sinSpy, EPS);

        assertEquals(-3.0, cotModule.calculate(1.0), EPS);
        assertEquals(1, cosSpy.getCallCount());
        assertEquals(1, sinSpy.getCallCount());
        assertEquals(1.0, cosSpy.getArguments().get(0), EPS);
        assertEquals(1.0, sinSpy.getArguments().get(0), EPS);
    }

    @Test
    void shouldReturnNaNWhenSinIsZero() {
        TrackingMathModuleSpy cosSpy = new TrackingMathModuleSpy(x -> 1.0);
        TrackingMathModuleSpy sinSpy = new TrackingMathModuleSpy(x -> 0.0);
        CotModule cotModule = new CotModule(cosSpy, sinSpy, EPS);

        assertTrue(Double.isNaN(cotModule.calculate(1.0)));
        assertEquals(1, cosSpy.getCallCount());
        assertEquals(1, sinSpy.getCallCount());
    }
}
