package de.tudresden.swt14ws18.util;

public class Util {
    public static boolean isBetween(int input, int lowerBound, int upperBound) {
        return input <= upperBound && input >= lowerBound;
    }

    public static int matches(int[] arr1, int[] arr2) {
        if (arr1 == null || arr1.length == 0 || arr2 == null || arr2.length == 0)
            return 0;

        int matches = 0;
        for (int i : arr1)
            for (int j : arr2) {
                if (i == j) {
                    matches++;
                    break;
                }
            }

        return matches;
    }

    public static boolean uniqueArray(int... arr) {
        for (int i : arr) {
            int matches = 0;
            for (int j : arr)
                if (i == j)
                    matches++;

            if (matches != 1)
                return false;
        }

        return true;
    }

    public static boolean isUnique(int[] numbers) {

        for (int i = 0; i < numbers.length; i++)
            for (int j = 0; j < numbers.length; j++) {
                if (i == j)
                    continue;

                if (numbers[i] == numbers[j])
                    return false;
            }
        return true;
    }

    public static <T> boolean arrayContains(int[] arr, int r) {
        if (arr != null && arr.length != 0)
            for (int ob : arr)
                if (ob == r)
                    return true;

        return false;
    }
}
