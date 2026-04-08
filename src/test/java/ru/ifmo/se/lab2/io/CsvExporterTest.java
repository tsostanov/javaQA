package ru.ifmo.se.lab2.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
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
}
