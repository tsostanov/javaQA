package ru.ifmo.se.lab2.support.spies;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import ru.ifmo.se.lab2.modules.MathModule;

public final class TrackingMathModuleSpy implements MathModule {
    private final DoubleUnaryOperator delegate;
    private final List<Double> arguments = new ArrayList<>();

    public TrackingMathModuleSpy(DoubleUnaryOperator delegate) {
        this.delegate = delegate;
    }

    @Override
    public double calculate(double x) {
        arguments.add(x);
        return delegate.applyAsDouble(x);
    }

    public int getCallCount() {
        return arguments.size();
    }

    public List<Double> getArguments() {
        return List.copyOf(arguments);
    }
}
