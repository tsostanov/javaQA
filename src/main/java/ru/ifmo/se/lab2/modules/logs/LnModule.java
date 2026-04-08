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
        if (Double.isNaN(x) || x <= 0.0 || Double.isInfinite(x)) {
            return Double.NaN;
        }

        double scaled = x;
        int scalePower = 0;

        while (scaled > UPPER_BOUND) {
            scaled /= 2.0;
            scalePower++;
        }
        while (scaled < LOWER_BOUND) {
            scaled *= 2.0;
            scalePower--;
        }

        double y = (scaled - 1.0) / (scaled + 1.0);
        double ySquared = y * y;
        double power = y;
        double sum = 0.0;
        int n = 0;

        while (true) {
            double term = power / (2 * n + 1);
            sum += term;
            if (Math.abs(term) < epsilon) {
                break;
            }
            power *= ySquared;
            n++;
            if (n > 1_000_000) {
                break;
            }
        }

        return 2.0 * sum + scalePower * LN_2;
    }
}
