package ru.ifmo.se.lab2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void shouldWriteCsvFromCli() throws IOException {
        Path output = Files.createTempFile("tpojava-main-", ".csv");

        Main.main(new String[]{"cos", "0", "1", "0.5", output.toString(), "1E-8"});

        String content = Files.readString(output);
        assertTrue(content.startsWith("x,result"));
        assertTrue(content.contains("0.5000000000"));
    }

    @Test
    void shouldPrintUsageWhenArgumentsAreMissing() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String printed = output.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Usage: java -jar"));
        assertTrue(printed.contains("Modules:"));
    }
}
