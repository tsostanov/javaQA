package ru.ifmo.se.lab2.modules.trig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.modules.MathModule;

class SinModuleTest {
    @Test
    void shouldBeDerivedFromCosModule() {
        MathModule fakeCos = x -> x;
        SinModule sinModule = new SinModule(fakeCos, 1.0E-8);

        assertEquals(Math.PI / 2.0 - 1.23, sinModule.calculate(1.23), 1.0E-12);
    }
}
