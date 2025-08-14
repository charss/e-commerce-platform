package com.example.shared.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MoneyUtil {
    public static String format(long amountInMinorUnits, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        int fractionDigits = currency.getDefaultFractionDigits();

        BigDecimal amount = BigDecimal.valueOf(amountInMinorUnits, fractionDigits);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(getLocaleForCurrency(currencyCode));

        formatter.setCurrency(currency);

        return formatter.format(amount);
    }

    private static Locale getLocaleForCurrency(String currencyCode) {
        return switch (currencyCode) {
            case "PHP" -> new Locale("en", "PH");
            case "USD" -> Locale.US;
            case "JPY" -> Locale.JAPAN;
            case "EUR" -> Locale.FRANCE;
            default -> new Locale("en", "PH");
        };
    }
}
