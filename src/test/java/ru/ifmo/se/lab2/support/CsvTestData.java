package ru.ifmo.se.lab2.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import ru.ifmo.se.lab2.support.stubs.TableMathModule;

public final class CsvTestData {
    private CsvTestData() {
    }

    public record Row(double x, double expected) {
    }

    public static Stream<Arguments> arguments(String resourcePath) {
        return rows(resourcePath).stream().map(row -> Arguments.of(row.x(), row.expected()));
    }

    public static List<Row> rows(String resourcePath) {
        try (InputStream input = CsvTestData.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Missing test resource: " + resourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .skip(1)
                        .filter(line -> !line.isBlank())
                        .map(CsvTestData::parseRow)
                        .toList();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read test resource: " + resourcePath, e);
        }
    }

    public static double expected(String resourcePath, double x) {
        Double expected = values(resourcePath).get(Double.doubleToLongBits(x));
        if (expected == null) {
            throw new IllegalArgumentException("Missing tabulated expected value in " + resourcePath + " for x=" + x);
        }
        return expected;
    }

    public static TableMathModule tableModule(String resourcePath, double... points) {
        Map<Long, Double> available = values(resourcePath);
        if (points.length == 0) {
            return TableMathModule.fromValues(available);
        }

        Set<Long> required = Arrays.stream(points)
                .mapToLong(Double::doubleToLongBits)
                .boxed()
                .collect(Collectors.toSet());
        Map<Long, Double> filtered = new LinkedHashMap<>();

        for (Map.Entry<Long, Double> entry : available.entrySet()) {
            if (required.remove(entry.getKey())) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }

        if (!required.isEmpty()) {
            throw new IllegalArgumentException("Missing tabulated values in " + resourcePath + " for " + required);
        }

        return TableMathModule.fromValues(filtered);
    }

    private static Map<Long, Double> values(String resourcePath) {
        Map<Long, Double> values = new LinkedHashMap<>();
        for (Row row : rows(resourcePath)) {
            values.put(Double.doubleToLongBits(row.x()), row.expected());
        }
        return values;
    }

    private static Row parseRow(String line) {
        String[] parts = line.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Expected 2 columns in line: " + line);
        }
        return new Row(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }
}
