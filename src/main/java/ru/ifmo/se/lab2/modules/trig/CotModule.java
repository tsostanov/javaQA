package ru.ifmo.se.lab2.modules.trig;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class CotModule extends AbstractMathModule {
    private final MathModule cosModule;
    private final MathModule sinModule;

    public CotModule(MathModule cosModule, MathModule sinModule, double epsilon) {
        super(epsilon);
        this.cosModule = cosModule;
        this.sinModule = sinModule;
    }

    @Override
    public double calculate(double x) {
        return safeDivide(cosModule.calculate(x), sinModule.calculate(x));
    }
}
