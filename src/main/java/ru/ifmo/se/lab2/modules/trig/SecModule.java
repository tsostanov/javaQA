package ru.ifmo.se.lab2.modules.trig;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class SecModule extends AbstractMathModule {
    private final MathModule cosModule;

    public SecModule(MathModule cosModule, double epsilon) {
        super(epsilon);
        this.cosModule = cosModule;
    }

    @Override
    public double calculate(double x) {
        return safeDivide(1.0, cosModule.calculate(x));
    }
}
