package de.tudresden.swt14ws18.gamemanagement;

import java.io.Serializable;
import java.util.Random;

import de.tudresden.swt14ws18.util.Util;

/**
 * Repräsentiert die Kombination von Lottozahlen, die man Tippen kann/gezogen werden können.
 */
public class LottoNumbers implements Serializable {
    private static final long serialVersionUID = -2668471664465713112L;

    private int superNumber;
    private int numbers[];

    /**
     * @param superNumber
     *            die Superzahl, muss zwischen 0 und 9 sein
     * @param numbers
     *            die Lottozahlen, müssen genau 6 und zwischen 1 und 49 sein (inklusive)
     */
    public LottoNumbers(int superNumber, int... numbers) {
        if (!Util.isBetween(superNumber, 0, 9))
            throw new IllegalArgumentException("SuperNumber must be between 0 and 9");

        if (numbers.length != 6)
            throw new IllegalArgumentException("LottoNumbers requires exactly 6 values!");

        if (!Util.isUnique(numbers))
            throw new IllegalArgumentException("LottoNumbers must be unique");

        for (int number : numbers)
            if (!Util.isBetween(number, 1, 49))
                throw new IllegalArgumentException("LottoNumbers must be between 1 and 49!");

        this.numbers = numbers;
        this.superNumber = superNumber;
    }

    /**
     * Hole die Superzahl der Nummern, ist immer zwischen 0 und 9 (inklusive)
     * 
     * @return die Superzahl
     */
    public int getSuperNumber() {
        return superNumber;
    }

    /**
     * Hole die 6 Zahlen des Spieles, welche immer zwischen 1 und 49 sind (inklusive).
     * 
     * @return ein int array mit genau 6 elementen
     */
    public int[] getNumbers() {
        return numbers;
    }

    /**
     * Vergleiche diese Nummern mit anderen Nummern und gebe die Übereinstimmungsklasse als LottoResult zurück.
     * 
     * @param number
     *            die Nummern mit denen vergliechen werden soll.
     * @return das Ergebnis des vergleiches als LottoResult
     */
    public LottoResult compare(LottoNumbers number) {
        return LottoResult.valueOf(Util.matches(getNumbers(), number.getNumbers()), getSuperNumber() == number.getSuperNumber());
    }

    /**
     * Zieht zufällt Lottozahlen.
     * 
     * @return eine LottoNumbers Instanz mit zufälligen Zahlen.
     */
    public static LottoNumbers draw() {
        Random rand = new Random();

        int[] arr = new int[6];

        for (int i = 0; i < 6; i++) {
            int r;

            do {
                r = rand.nextInt(49) + 1;
            } while (Util.arrayContains(arr, r));

            arr[i] = r;
        }
        int s = rand.nextInt(10);
        return new LottoNumbers(s, arr);
    }

    /**
     * Erstellt ein String mit dem Format "Getippte Zahlen: x x x x x x"
     * 
     * @return der String
     */
    public String getNumbersAsString() {
        StringBuilder numbers_temp = new StringBuilder();

        numbers_temp.append("  Getippte Zahlen: ");

        for (int i : getNumbers()) {
            numbers_temp.append(i);
            numbers_temp.append(" ");
        }

        return numbers_temp.toString();
    }

    /**
     * Erstellt ein String mit dem Format "Getippte Superzahl: x"
     * 
     * @return der String
     */
    public String getSuperNumberAsString() {
        StringBuilder temp = new StringBuilder();

        temp.append("Getippte Superzahl: ");
        temp.append(getSuperNumber());

        return temp.toString();
    }
}
