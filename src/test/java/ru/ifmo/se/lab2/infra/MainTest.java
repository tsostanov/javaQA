package ru.ifmo.se.lab2.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ifmo.se.lab2.Main;
import ru.ifmo.se.lab2.modules.system.FunctionSystemModule;
import ru.ifmo.se.lab2.modules.trig.CosModule;
import ru.ifmo.se.lab2.modules.trig.CotModule;
import ru.ifmo.se.lab2.modules.trig.CscModule;
import ru.ifmo.se.lab2.modules.trig.SecModule;
import ru.ifmo.se.lab2.modules.trig.SinModule;
import ru.ifmo.se.lab2.modules.logs.LnModule;
import ru.ifmo.se.lab2.modules.logs.LogModule;

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

    @Test
    void shouldResolveDefaultOutputPath() throws Exception {
        Path output = (Path) resolveOutputMethod().invoke(null, "cos", new String[]{"cos", "0", "1", "0.5"});

        assertEquals(Path.of("cos.csv"), output);
    }

    @Test
    void shouldResolveProvidedOutputPath() throws Exception {
        Path output = (Path) resolveOutputMethod().invoke(
                null, "cos", new String[]{"cos", "0", "1", "0.5", "custom.csv"}
        );

        assertEquals(Path.of("custom.csv"), output);
    }

    @Test
    void shouldResolveDefaultEpsilon() throws Exception {
        double epsilon = (double) resolveEpsilonMethod().invoke(null, (Object) new String[]{"cos", "0", "1", "0.5"});

        assertEquals(1.0E-8, epsilon);
    }

    @Test
    void shouldResolveProvidedEpsilon() throws Exception {
        double epsilon = (double) resolveEpsilonMethod().invoke(
                null, (Object) new String[]{"cos", "0", "1", "0.5", "custom.csv", "1E-6"}
        );

        assertEquals(1.0E-6, epsilon);
    }

    @ParameterizedTest
    @CsvSource({
            "cos, CosModule",
            "sin, SinModule",
            "sec, SecModule",
            "csc, CscModule",
            "cot, CotModule",
            "ln, LnModule",
            "log2, LogModule",
            "log3, LogModule",
            "log5, LogModule",
            "log10, LogModule",
            "system, FunctionSystemModule"
    })
    void shouldCreateRequestedModule(String moduleName, String expectedType) throws Exception {
        Object module = createModuleMethod().invoke(null, moduleName, 1.0E-8);

        switch (expectedType) {
            case "CosModule" -> assertInstanceOf(CosModule.class, module);
            case "SinModule" -> assertInstanceOf(SinModule.class, module);
            case "SecModule" -> assertInstanceOf(SecModule.class, module);
            case "CscModule" -> assertInstanceOf(CscModule.class, module);
            case "CotModule" -> assertInstanceOf(CotModule.class, module);
            case "LnModule" -> assertInstanceOf(LnModule.class, module);
            case "LogModule" -> assertInstanceOf(LogModule.class, module);
            case "FunctionSystemModule" -> assertInstanceOf(FunctionSystemModule.class, module);
            default -> throw new AssertionError("Unexpected expectedType: " + expectedType);
        }
    }

    @Test
    void shouldRejectUnknownModule() throws Exception {
        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                () -> createModuleMethod().invoke(null, "unknown", 1.0E-8)
        );

        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
        assertTrue(exception.getCause().getMessage().contains("Unknown module"));
    }

    private static Method createModuleMethod() throws NoSuchMethodException {
        Method method = Main.class.getDeclaredMethod("createModule", String.class, double.class);
        method.setAccessible(true);
        return method;
    }

    private static Method resolveOutputMethod() throws NoSuchMethodException {
        Method method = Main.class.getDeclaredMethod("resolveOutput", String.class, String[].class);
        method.setAccessible(true);
        return method;
    }

    private static Method resolveEpsilonMethod() throws NoSuchMethodException {
        Method method = Main.class.getDeclaredMethod("resolveEpsilon", String[].class);
        method.setAccessible(true);
        return method;
    }
}
