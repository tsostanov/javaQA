package ru.ifmo.se.lab2.support;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import ru.ifmo.se.lab2.modules.MathModule;

public final class MockitoMathModuleFactory {
    private MockitoMathModuleFactory() {
    }

    public static MathModule tabulated(String resourcePath, double... points) {
        Map<Long, Double> values = CsvTestData.valueMap(resourcePath, points);
        MathModule module = mock(MathModule.class);
        when(module.calculate(anyDouble())).thenAnswer(invocation -> {
            double x = invocation.getArgument(0);
            Double value = values.get(Double.doubleToLongBits(x));
            if (value == null) {
                throw new IllegalArgumentException("No tabulated Mockito stub value for x=" + x + " in " + resourcePath);
            }
            return value;
        });
        return module;
    }

    public static MathModule constant(double value) {
        MathModule module = mock(MathModule.class);
        when(module.calculate(anyDouble())).thenReturn(value);
        return module;
    }

    public static MathModule nan() {
        return constant(Double.NaN);
    }
}
