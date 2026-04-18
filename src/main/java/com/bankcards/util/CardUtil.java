package com.bankcards.util;

import com.bankcards.entity.enums.Currency;

import java.util.Random;

public final class CardUtil {

    // Blocking the creation of an instance of a utility class
    private CardUtil() {
    }

    // Default currency logic (RUB)
    public static Currency parseCurrencyOrDefault(String currency) {
        if (currency == null || currency.isBlank()) {
            return Currency.RUB;
        }
        try {
            return Currency.valueOf(currency.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported currency: " + currency);
        }
    }

    // random digit generation
    public static String randomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    // Masks card number before sending it to client for security reasons
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }

        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFourDigits;
    }
}
