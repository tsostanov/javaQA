package ru.ifmo.se.lab2.modules.trig.integration;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import ru.ifmo.se.lab2.support.CsvTestData;

final class TrigIntegrationSupport {
    static final double EPS = 1.0E-6;
    static final double PRECISION = 1.0E-10;
    static final double[] TRIG_POINTS = {-2.3, -1.2, -0.7};

    private TrigIntegrationSupport() {
    }

    static double[] transformedArguments(double[] points) {
        double[] result = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            result[i] = Math.PI / 2.0 - points[i];
        }
        return result;
    }

    static Stream<Arguments> cases(String resourcePath) {
        return CsvTestData.rows(resourcePath).stream()
                .filter(row -> containsPoint(row.x()))
                .map(row -> Arguments.of(row.x(), row.expected()));
    }

    private static boolean containsPoint(double x) {
        for (double point : TRIG_POINTS) {
            if (Double.doubleToLongBits(point) == Double.doubleToLongBits(x)) {
                return true;
            }
        }
        return false;
    }
}
