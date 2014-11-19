package de.tudresden.swt14ws18.util;

public class Util {
    public static boolean isBetween(int input, int lowerBound, int upperBound) {
	return input <= upperBound && input >= lowerBound;
    }
}
