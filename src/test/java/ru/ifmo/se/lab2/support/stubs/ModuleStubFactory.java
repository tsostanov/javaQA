package ru.ifmo.se.lab2.support.stubs;

import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.support.CsvTestData;

public final class ModuleStubFactory {
    private static final String COS_VALUES = "/testdata/integration/trig/cos-stub.csv";
    private static final String SIN_VALUES = "/testdata/integration/trig/sin-stub.csv";
    private static final String SEC_VALUES = "/testdata/integration/trig/sec-stub.csv";
    private static final String CSC_VALUES = "/testdata/integration/trig/csc-stub.csv";
    private static final String COT_VALUES = "/testdata/integration/trig/cot-stub.csv";
    private static final String LN_VALUES = "/testdata/module/logs/ln.csv";

    private ModuleStubFactory() {
    }

    public static MathModule cosStub(double... points) {
        return CsvTestData.tableModule(COS_VALUES, points);
    }

    public static MathModule sinStub(double... points) {
        return CsvTestData.tableModule(SIN_VALUES, points);
    }

    public static MathModule secStub(double... points) {
        return CsvTestData.tableModule(SEC_VALUES, points);
    }

    public static MathModule cscStub(double... points) {
        return CsvTestData.tableModule(CSC_VALUES, points);
    }

    public static MathModule cotStub(double... points) {
        return CsvTestData.tableModule(COT_VALUES, points);
    }

    public static MathModule lnStub(double... points) {
        return CsvTestData.tableModule(LN_VALUES, points);
    }

    public static MathModule logStub(double base, double... points) {
        return CsvTestData.tableModule(resourceForBase(base), points);
    }

    private static String resourceForBase(double base) {
        if (base == 2.0) {
            return "/testdata/module/logs/log2.csv";
        }
        if (base == 3.0) {
            return "/testdata/module/logs/log3.csv";
        }
        if (base == 5.0) {
            return "/testdata/module/logs/log5.csv";
        }
        if (base == 10.0) {
            return "/testdata/module/logs/log10.csv";
        }
        throw new IllegalArgumentException("No tabulated stub values for base=" + base);
    }
}
