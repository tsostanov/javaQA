package ru.ifmo.se.lab2.modules;

public abstract class AbstractMathModule implements MathModule {
    protected final double epsilon;

    protected AbstractMathModule(double epsilon) {
        if (epsilon <= 0.0) {
            throw new IllegalArgumentException("epsilon must be positive");
        }
        this.epsilon = epsilon;
    }

    protected double safeDivide(double numerator, double denominator) {
        if (Double.isNaN(numerator) || Double.isNaN(denominator) || Math.abs(denominator) < epsilon) {
            return Double.NaN;
        }
        return numerator / denominator;
    }
}
