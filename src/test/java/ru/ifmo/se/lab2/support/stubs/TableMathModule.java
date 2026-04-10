package ru.ifmo.se.lab2.support.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import ru.ifmo.se.lab2.modules.MathModule;

public final class TableMathModule implements MathModule {
    private final Map<Long, Double> values;

    private TableMathModule(Map<Long, Double> values) {
        this.values = values;
    }

    public static TableMathModule fromFunction(DoubleUnaryOperator function, double... points) {
        Map<Long, Double> values = new HashMap<>();
        for (double point : points) {
            values.put(Double.doubleToLongBits(point), function.applyAsDouble(point));
        }
        return new TableMathModule(values);
    }

    public static TableMathModule fromValues(Map<Long, Double> values) {
        return new TableMathModule(new HashMap<>(values));
    }

    @Override
    public double calculate(double x) {
        Double value = values.get(Double.doubleToLongBits(x));
        if (value == null) {
            throw new IllegalArgumentException("No tabular stub value for x=" + x);
        }
        return value;
    }
}
