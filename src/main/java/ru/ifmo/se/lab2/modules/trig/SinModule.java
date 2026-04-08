package ru.ifmo.se.lab2.modules.trig;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class SinModule extends AbstractMathModule {
    private final MathModule cosModule;

    public SinModule(MathModule cosModule, double epsilon) {
        super(epsilon);
        this.cosModule = cosModule;
    }

    @Override
    public double calculate(double x) {
        return cosModule.calculate(Math.PI / 2.0 - x);
    }
}
