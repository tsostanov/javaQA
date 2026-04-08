package ru.ifmo.se.lab2.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import ru.ifmo.se.lab2.modules.MathModule;

public final class CsvExporter {
    public void export(MathModule module, double from, double to, double step, Path output) throws IOException {
        if (step <= 0.0) {
            throw new IllegalArgumentException("Step must be positive");
        }
        if (from > to) {
            throw new IllegalArgumentException("from must be <= to");
        }

        List<String> rows = new ArrayList<>();
        rows.add("x,result");

        for (double x = from; x <= to + step / 2.0; x += step) {
            double result = module.calculate(x);
            rows.add(String.format(Locale.US, "%.10f,%.10f", x, result));
        }

        Files.write(output, rows, StandardCharsets.UTF_8);
    }
}
