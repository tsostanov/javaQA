package ru.ifmo.se.lab2.infra.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import ru.ifmo.se.lab2.io.CsvExporter;
import ru.ifmo.se.lab2.modules.MathModule;

class CsvExporterTest {
    @Test
    void shouldWriteCsvFile() throws IOException {
        Path file = Files.createTempFile("tpojava-", ".csv");
        CsvExporter exporter = new CsvExporter();
        MathModule module = x -> x * x;

        exporter.export(module, 0.0, 1.0, 0.5, file);

        String content = Files.readString(file);
        assertTrue(content.startsWith("x,result"));
        assertTrue(content.contains("0.5000000000,0.2500000000"));
        assertEquals(4, content.lines().count());
    }

    @Test
    void shouldRejectNonPositiveStep() {
        CsvExporter exporter = new CsvExporter();

        assertThrows(IllegalArgumentException.class, () -> exporter.export(x -> x, 0.0, 1.0, 0.0, Path.of("tmp.csv")));
    }

    @Test
    void shouldRejectInvalidRange() {
        CsvExporter exporter = new CsvExporter();

        assertThrows(IllegalArgumentException.class, () -> exporter.export(x -> x, 2.0, 1.0, 0.5, Path.of("tmp.csv")));
    }

    @Test
    void shouldWriteNaNValues() throws IOException {
        Path file = Files.createTempFile("tpojava-", ".csv");
        CsvExporter exporter = new CsvExporter();
        MathModule module = x -> Double.NaN;

        exporter.export(module, 0.0, 0.0, 1.0, file);

        String content = Files.readString(file);
        assertTrue(content.contains("0.0000000000,NaN"));
    }
}
