package ru.ifmo.se.lab2.stubs;

import ru.ifmo.se.lab2.modules.MathModule;

public final class ModuleStubFactory {
    private ModuleStubFactory() {
    }

    public static MathModule cosStub(double... points) {
        return TableMathModule.fromFunction(Math::cos, points);
    }

    public static MathModule sinStub(double... points) {
        return TableMathModule.fromFunction(Math::sin, points);
    }

    public static MathModule secStub(double... points) {
        return TableMathModule.fromFunction(x -> 1.0 / Math.cos(x), points);
    }

    public static MathModule cscStub(double... points) {
        return TableMathModule.fromFunction(x -> 1.0 / Math.sin(x), points);
    }

    public static MathModule cotStub(double... points) {
        return TableMathModule.fromFunction(x -> Math.cos(x) / Math.sin(x), points);
    }

    public static MathModule lnStub(double... points) {
        return TableMathModule.fromFunction(Math::log, points);
    }

    public static MathModule logStub(double base, double... points) {
        return TableMathModule.fromFunction(x -> Math.log(x) / Math.log(base), points);
    }
}
