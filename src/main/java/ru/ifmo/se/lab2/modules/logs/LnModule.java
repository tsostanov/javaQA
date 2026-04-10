package ru.ifmo.se.lab2.modules.logs;

import ru.ifmo.se.lab2.modules.AbstractMathModule;

public final class LnModule extends AbstractMathModule {
    private static final double LOWER_BOUND = 0.75;
    private static final double UPPER_BOUND = 1.5;
    private static final double LN_2 = 0.6931471805599453;

    public LnModule(double epsilon) {
        super(epsilon);
    }

    @Override
    public double calculate(double x) {
        if (isInvalidInput(x)) {
            return Double.NaN;
        }

        ScaleResult scaled = scaleToWorkingRange(x);
        return seriesLn(scaled.value()) + scaled.powerOfTwo() * LN_2;
    }

    private boolean isInvalidInput(double x) {
        return Double.isNaN(x) || x <= 0.0 || Double.isInfinite(x);
    }

    private ScaleResult scaleToWorkingRange(double x) {
        double value = x;
        int powerOfTwo = 0;

        while (value > UPPER_BOUND) {
            value /= 2.0;
            powerOfTwo++;
        }
        while (value < LOWER_BOUND) {
            value *= 2.0;
            powerOfTwo--;
        }

        return new ScaleResult(value, powerOfTwo);
    }

    private double seriesLn(double x) {
        double y = (x - 1.0) / (x + 1.0);
        double ySquared = y * y;
        double oddPower = y;
        double sum = 0.0;

        for (int n = 0; ; n++) {
            double term = oddPower / (2 * n + 1);
            sum += term;
            if (Math.abs(term) < epsilon) {
                return 2.0 * sum;
            }
            oddPower *= ySquared;
        }
    }

    private record ScaleResult(double value, int powerOfTwo) {
    }
}
