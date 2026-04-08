package ru.ifmo.se.lab2.modules.trig;

import ru.ifmo.se.lab2.modules.AbstractMathModule;

public final class CosModule extends AbstractMathModule {
    private static final double TWO_PI = 2.0 * Math.PI;

    public CosModule(double epsilon) {
        super(epsilon);
    }

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }

        double normalized = normalizeAngle(x);
        double term = 1.0;
        double sum = 1.0;
        int n = 1;

        while (Math.abs(term) > epsilon) {
            term *= -normalized * normalized / ((2.0 * n - 1.0) * (2.0 * n));
            sum += term;
            n++;
            if (n > 10_000) {
                break;
            }
        }

        return sum;
    }

    private double normalizeAngle(double x) {
        double normalized = x % TWO_PI;
        if (normalized > Math.PI) {
            normalized -= TWO_PI;
        } else if (normalized < -Math.PI) {
            normalized += TWO_PI;
        }
        return normalized;
    }
}
