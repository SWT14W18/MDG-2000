package de.tudresden.swt14ws18.gamemanagement;

public enum TotoResult {
    WIN_HOME,
    WIN_GUEST,
    DRAW,
    NOT_PLAYED;

    public static TotoResult parseString(String string) {
	switch (string) {
	case "0":
	    return WIN_HOME;
	case "1":
	    return WIN_GUEST;
	case "2":
	    return DRAW;
	case "3":
	    return NOT_PLAYED;
	default:
	    return null;
	}
    }
}
