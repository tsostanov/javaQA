package ru.ifmo.se.lab2;

import java.io.IOException;
import java.nio.file.Path;
import ru.ifmo.se.lab2.io.CsvExporter;
import ru.ifmo.se.lab2.modules.MathModule;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            printUsage();
            return;
        }

        String moduleName = args[0];
        double from = Double.parseDouble(args[1]);
        double to = Double.parseDouble(args[2]);
        double step = Double.parseDouble(args[3]);
        Path output = resolveOutput(moduleName, args);
        double epsilon = resolveEpsilon(args);

        MathModule module = createModule(moduleName, epsilon);
        CsvExporter exporter = new CsvExporter();
        exporter.export(module, from, to, step, output);
    }

    private static Path resolveOutput(String moduleName, String[] args) {
        return args.length >= 5 ? Path.of(args[4]) : Path.of(moduleName + ".csv");
    }

    private static double resolveEpsilon(String[] args) {
        return args.length >= 6 ? Double.parseDouble(args[5]) : 1.0E-8;
    }

    private static MathModule createModule(String moduleName, double epsilon) {
        CosModule cos = new CosModule(epsilon);
        SinModule sin = new SinModule(cos, epsilon);
        SecModule sec = new SecModule(cos, epsilon);
        CscModule csc = new CscModule(sin, epsilon);
        CotModule cot = new CotModule(cos, sin, epsilon);
        LnModule ln = new LnModule(epsilon);
        LogModule log2 = new LogModule(2.0, ln, epsilon);
        LogModule log3 = new LogModule(3.0, ln, epsilon);
        LogModule log5 = new LogModule(5.0, ln, epsilon);
        LogModule log10 = new LogModule(10.0, ln, epsilon);

        return switch (moduleName.toLowerCase()) {
            case "cos" -> cos;
            case "sin" -> sin;
            case "sec" -> sec;
            case "csc" -> csc;
            case "cot" -> cot;
            case "ln" -> ln;
            case "log2" -> log2;
            case "log3" -> log3;
            case "log5" -> log5;
            case "log10" -> log10;
            case "system" -> new FunctionSystemModule(cos, sin, sec, csc, cot, ln, log2, log3, log5, log10, epsilon);
            default -> throw new IllegalArgumentException("Unknown module: " + moduleName);
        };
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar tpojava.jar <module> <from> <to> <step> [output.csv] [epsilon]");
        System.out.println("Modules: system, cos, sin, sec, csc, cot, ln, log2, log3, log5, log10");
    }
}
