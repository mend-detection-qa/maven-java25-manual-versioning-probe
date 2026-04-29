package com.example.probe.app;

import com.example.probe.core.Money;
import com.example.probe.core.Shape;
import com.example.probe.core.Shapes;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal JUnit 5 test so junit-jupiter is actually referenced at compile/test time.
 */
class MainTest {

    @Test
    void moneyAddSameCurrency() {
        Money a = new Money(new BigDecimal("1.00"), "EUR");
        Money b = new Money(new BigDecimal("2.50"), "EUR");
        Money sum = a.add(b);
        assertEquals(new BigDecimal("3.50"), sum.amount());
        assertEquals("EUR", sum.currency());
    }

    @Test
    void moneyRejectsDifferentCurrencies() {
        Money usd = new Money(new BigDecimal("1.00"), "USD");
        Money eur = new Money(new BigDecimal("1.00"), "EUR");
        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    void shapesDescribeCircle() {
        Shape circle = new Shape.Circle(1.0);
        String desc = Shapes.describe(circle);
        assertTrue(desc.contains("Circle"), "Expected 'Circle' in: " + desc);
    }

    @Test
    void shapesDescribeSquare() {
        Shape square = new Shape.Square(2.0);
        String desc = Shapes.describe(square);
        assertTrue(desc.contains("Square"), "Expected 'Square' in: " + desc);
    }
}