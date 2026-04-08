package ru.ifmo.se.lab2.modules.trig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.mocks.VerifiableMathModuleMock;

class SinModuleTest {
    @Test
    void shouldBeDerivedFromCosModule() {
        double shiftedArgument = Math.PI / 2.0 - 1.23;
        VerifiableMathModuleMock cosMock = new VerifiableMathModuleMock()
                .expectCall(shiftedArgument, 7.5);
        SinModule sinModule = new SinModule(cosMock, 1.0E-8);

        assertEquals(7.5, sinModule.calculate(1.23), 1.0E-12);
        cosMock.verifyCalled(shiftedArgument);
        cosMock.verifyCallCount(1);
    }
}
