package ru.ifmo.se.lab2.modules.trig;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class CscModule extends AbstractMathModule {
    private final MathModule sinModule;

    public CscModule(MathModule sinModule, double epsilon) {
        super(epsilon);
        this.sinModule = sinModule;
    }

    @Override
    public double calculate(double x) {
        return safeDivide(1.0, sinModule.calculate(x));
    }
}
