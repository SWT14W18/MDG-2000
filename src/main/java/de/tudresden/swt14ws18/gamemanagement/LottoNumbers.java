package de.tudresden.swt14ws18.gamemanagement;

import de.tudresden.swt14ws18.util.Util;

public class LottoNumbers {
    private int superNumber;
    private int numbers[];

    public LottoNumbers(int superNumber, int... numbers) {
	if (!Util.isBetween(superNumber, 0, 9))
	    throw new IllegalArgumentException(
		    "SuperNumber must be between 0 and 9");

	if (numbers.length != 6)
	    throw new IllegalArgumentException(
		    "LottoNumbers requires exactly 7 values!");

	for (int number : numbers)
	    if (!Util.isBetween(number, 1, 49))
		throw new IllegalArgumentException(
			"LottoNumbers must be between 1 and 49!");

	this.numbers = numbers;
    }

    public int getSuperNumber() {
	return superNumber;
    }

    public int[] getNumbers() {
	return numbers;
    }
    
    public LottoResult compare(LottoNumbers number) {
	
	return LottoResult.valueOf(Util.matches(getNumbers(), number.getNumbers()), getSuperNumber() == number.getSuperNumber());
    }
}
