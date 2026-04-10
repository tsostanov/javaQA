package ru.ifmo.se.lab2.support.mocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.ifmo.se.lab2.modules.MathModule;

public final class VerifiableMathModuleMock implements MathModule {
    private final Map<Long, Double> expectedResults = new HashMap<>();
    private final Set<Long> calledArguments = new HashSet<>();
    private int callCount;

    public VerifiableMathModuleMock expectCall(double argument, double result) {
        expectedResults.put(Double.doubleToLongBits(argument), result);
        return this;
    }

    @Override
    public double calculate(double x) {
        long key = Double.doubleToLongBits(x);
        callCount++;
        calledArguments.add(key);

        if (!expectedResults.containsKey(key)) {
            throw new AssertionError("Unexpected mock call for x=" + x);
        }

        return expectedResults.get(key);
    }

    public void verifyCalled(double argument) {
        assertTrue(calledArguments.contains(Double.doubleToLongBits(argument)), "Expected call for x=" + argument);
    }

    public void verifyCallCount(int expectedCallCount) {
        assertEquals(expectedCallCount, callCount, "Unexpected number of mock invocations");
    }
}
