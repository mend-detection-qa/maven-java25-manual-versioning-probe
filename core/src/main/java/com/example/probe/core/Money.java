package com.example.probe.core;

import java.math.BigDecimal;

/**
 * Immutable monetary value. Demonstrates Java 16+ record syntax.
 * The compact constructor validates that amount is non-negative.
 */
public record Money(BigDecimal amount, String currency) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be non-negative, got: " + amount);
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        currency = currency.toUpperCase();
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "Cannot add Money with different currencies: " + this.currency + " vs " + other.currency
            );
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}