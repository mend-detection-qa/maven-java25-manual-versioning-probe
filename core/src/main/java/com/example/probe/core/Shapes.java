package com.example.probe.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that uses Java 21+ pattern-matching switch (stable in Java 25)
 * over the sealed Shape hierarchy.
 */
public final class Shapes {

    private static final Logger LOG = LoggerFactory.getLogger(Shapes.class);

    private Shapes() {}

    /**
     * Returns a human-readable description of the given shape using
     * pattern-matching switch — stable syntax in Java 25.
     */
    public static String describe(Shape shape) {
        return switch (shape) {
            case Shape.Circle c  -> String.format("Circle with radius %.2f and area %.4f", c.radius(), c.area());
            case Shape.Square s  -> String.format("Square with side %.2f and area %.4f", s.side(), s.area());
        };
    }

    /**
     * Logs the description of each shape in the provided array.
     */
    public static void logAll(Shape... shapes) {
        for (Shape shape : shapes) {
            LOG.info(describe(shape));
        }
    }
}