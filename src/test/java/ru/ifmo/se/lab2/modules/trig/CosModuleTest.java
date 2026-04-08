package ru.ifmo.se.lab2.modules.trig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CosModuleTest {
    private static final double EPS = 1.0E-6;
    private final CosModule cosModule = new CosModule(1.0E-10);

    @ParameterizedTest
    @ValueSource(doubles = {-3.0, -Math.PI, -1.0, -0.5, 0.0, 0.5, 1.0, Math.PI / 3.0, Math.PI})
    void shouldApproximateMathCos(double x) {
        assertEquals(Math.cos(x), cosModule.calculate(x), EPS);
    }
}
