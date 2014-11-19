package de.tudresden.swt14ws18.gamemanagement;

public enum LottoResult {
    NONE,
    TWO_SUPER,
    THREE,
    THREE_SUPER,
    FOUR,
    FOUR_SUPER,
    FIVE,
    FIVE_SUPER,
    SIX,
    SIX_SUPER;

    public static LottoResult valueOf(int matches, boolean matchedSuperNumber) {
	switch (matches) {
	case 2:
	    return matchedSuperNumber ? TWO_SUPER : NONE;
	case 3:
	    return matchedSuperNumber ? THREE_SUPER : THREE;
	case 4:
	    return matchedSuperNumber ? FOUR_SUPER : FOUR;
	case 5:
	    return matchedSuperNumber ? FIVE_SUPER : FIVE;
	case 6:
	    return matchedSuperNumber ? SIX_SUPER : SIX;
	default:
	    return NONE;
	}
    }
}
