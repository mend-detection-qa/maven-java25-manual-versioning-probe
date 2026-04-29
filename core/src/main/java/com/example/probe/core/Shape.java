package com.example.probe.core;

/**
 * Sealed interface hierarchy demonstrating Java 17+ sealed types (stable in Java 25).
 *
 * Permitted subtypes:
 *   - Circle  (record)
 *   - Square  (record)
 */
public sealed interface Shape permits Shape.Circle, Shape.Square {

    /** Returns the area of the shape. */
    double area();

    /** A circle defined by its radius. */
    record Circle(double radius) implements Shape {
        public Circle {
            if (radius <= 0) {
                throw new IllegalArgumentException("radius must be positive, got: " + radius);
            }
        }

        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    /** A square defined by its side length. */
    record Square(double side) implements Shape {
        public Square {
            if (side <= 0) {
                throw new IllegalArgumentException("side must be positive, got: " + side);
            }
        }

        @Override
        public double area() {
            return side * side;
        }
    }
}