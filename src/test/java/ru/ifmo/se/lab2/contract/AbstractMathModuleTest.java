package ru.ifmo.se.lab2.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.AbstractMathModule;

class AbstractMathModuleTest {
    @Test
    void shouldRejectNonPositiveEpsilon() {
        assertThrows(IllegalArgumentException.class, () -> new TestMathModule(0.0));
        assertThrows(IllegalArgumentException.class, () -> new TestMathModule(-1.0));
    }

    @Test
    void shouldDivideFiniteValues() {
        TestMathModule module = new TestMathModule(1.0E-8);

        assertEquals(2.5, module.safeDivideForTest(5.0, 2.0));
    }

    @Test
    void shouldReturnNaNWhenNumeratorIsNaN() {
        TestMathModule module = new TestMathModule(1.0E-8);

        assertTrue(Double.isNaN(module.safeDivideForTest(Double.NaN, 2.0)));
    }

    @Test
    void shouldReturnNaNWhenDenominatorIsNaN() {
        TestMathModule module = new TestMathModule(1.0E-8);

        assertTrue(Double.isNaN(module.safeDivideForTest(5.0, Double.NaN)));
    }

    @Test
    void shouldReturnNaNWhenDenominatorIsTooSmall() {
        TestMathModule module = new TestMathModule(1.0E-8);

        assertTrue(Double.isNaN(module.safeDivideForTest(5.0, 1.0E-9)));
    }

    private static final class TestMathModule extends AbstractMathModule {
        private TestMathModule(double epsilon) {
            super(epsilon);
        }

        @Override
        public double calculate(double x) {
            return x;
        }

        private double safeDivideForTest(double numerator, double denominator) {
            return safeDivide(numerator, denominator);
        }
    }
}
