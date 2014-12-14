package de.tudresden.swt14ws18.util;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.salespointframework.useraccount.Role;

public final class Constants {
    public static final Role ADMIN = new Role("ROLE_BOSS");
    public static final Role USER = new Role("ROLE_USER");
    public static final Role CUSTOMER = new Role("ROLE_CUSTOMER");
    public static final Role CUSTOMER_BLOCKABLE = new Role("ROLE_CUSTOMER_BLOCKABLE");
    public static final Role ANONYM = new Role("ROLE_ANONYM");
    public static final Role TOTO_LIST = new Role("ROLE_TOTOLIST");
    public static final long MINUTES_BEFORE_DATE = 5;
    public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#0.00");

    /**
     * Standard Datumsformat für die allgemeine Zeitausgabe.
     */
    public static final DateTimeFormatter OUTPUT_DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
    public static final double LOTTO_PRICE = 1.00D;
    public static final double INPUT_INTO_POT = 0.9D;
}
