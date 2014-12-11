package de.tudresden.swt14ws18.util;

import java.text.DecimalFormat;

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
}
