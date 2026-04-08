package ru.ifmo.se.lab2.modules.logs;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class LogModule extends AbstractMathModule {
    private final double base;
    private final MathModule lnModule;
    private final double lnBase;

    public LogModule(double base, MathModule lnModule, double epsilon) {
        super(epsilon);
        if (base <= 0.0 || base == 1.0) {
            throw new IllegalArgumentException("base must be positive and not equal to 1");
        }
        this.base = base;
        this.lnModule = lnModule;
        this.lnBase = lnModule.calculate(base);
    }

    @Override
    public double calculate(double x) {
        return safeDivide(lnModule.calculate(x), lnBase);
    }

    public double getBase() {
        return base;
    }
}
