package com.example.probe.app;

import com.example.probe.core.Money;
import com.example.probe.core.Shape;
import com.example.probe.core.Shapes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Application entry point.
 * Uses standard {@code public static void main} (no preview flags needed).
 */
public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {}

    public static void main(String[] args) {
        LOG.info("maven-java25-manual-versioning-probe starting");

        // Exercise the Money record
        Money price  = new Money(new BigDecimal("9.99"),  "usd");
        Money tax    = new Money(new BigDecimal("0.80"),  "usd");
        Money total  = price.add(tax);
        LOG.info("Total: {} {}", total.amount(), total.currency());

        // Exercise sealed Shape hierarchy + pattern-matching switch
        Shape circle = new Shape.Circle(3.0);
        Shape square = new Shape.Square(4.0);
        Shapes.logAll(circle, square);

        LOG.info("maven-java25-manual-versioning-probe finished");
    }
}