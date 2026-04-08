package ru.ifmo.se.lab2.modules.system;

import ru.ifmo.se.lab2.modules.AbstractMathModule;
import ru.ifmo.se.lab2.modules.MathModule;

public final class FunctionSystemModule extends AbstractMathModule {
    private final MathModule cosModule;
    private final MathModule sinModule;
    private final MathModule secModule;
    private final MathModule cscModule;
    private final MathModule cotModule;
    private final MathModule lnModule;
    private final MathModule log2Module;
    private final MathModule log3Module;
    private final MathModule log5Module;
    private final MathModule log10Module;

    public FunctionSystemModule(
            MathModule cosModule,
            MathModule sinModule,
            MathModule secModule,
            MathModule cscModule,
            MathModule cotModule,
            MathModule lnModule,
            MathModule log2Module,
            MathModule log3Module,
            MathModule log5Module,
            MathModule log10Module,
            double epsilon
    ) {
        super(epsilon);
        this.cosModule = cosModule;
        this.sinModule = sinModule;
        this.secModule = secModule;
        this.cscModule = cscModule;
        this.cotModule = cotModule;
        this.lnModule = lnModule;
        this.log2Module = log2Module;
        this.log3Module = log3Module;
        this.log5Module = log5Module;
        this.log10Module = log10Module;
    }

    @Override
    public double calculate(double x) {
        return x <= 0.0 ? calculateTrigBranch(x) : calculateLogBranch(x);
    }

    private double calculateTrigBranch(double x) {
        double sin = sinModule.calculate(x);
        double cos = cosModule.calculate(x);
        double sec = secModule.calculate(x);
        double csc = cscModule.calculate(x);
        double cot = cotModule.calculate(x);

        if (Double.isNaN(sin) || Double.isNaN(cos) || Double.isNaN(sec) || Double.isNaN(csc) || Double.isNaN(cot)) {
            return Double.NaN;
        }

        double numerator = Math.pow((((safeDivide(csc, csc) / sec) - sin) + cot), 3.0) + (cos + csc) - (sec - sin);
        double denominator = (safeDivide(Math.pow(cot, 2.0), csc + sec)) * Math.pow(sin, 2.0);
        return safeDivide(Math.pow(safeDivide(numerator, denominator), 2.0), cot);
    }

    private double calculateLogBranch(double x) {
        double ln = lnModule.calculate(x);
        double log2 = log2Module.calculate(x);
        double log3 = log3Module.calculate(x);
        double log5 = log5Module.calculate(x);
        double log10 = log10Module.calculate(x);

        if (Double.isNaN(ln) || Double.isNaN(log2) || Double.isNaN(log3) || Double.isNaN(log5) || Double.isNaN(log10)) {
            return Double.NaN;
        }

        return (log10 * log10 * log2) + log3 + log5 + Math.pow(ln, 3.0);
    }
}
