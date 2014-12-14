package de.tudresden.swt14ws18.gamemanagement;

/**
 * Repräsentiert die möglichen Ergebnisse eines Fußballspieles.
 */
public enum TotoResult {
    WIN_HOME("Heimsieg"),
    WIN_GUEST("Auswärtssieg"),
    DRAW("Unentschieden"),
    NOT_PLAYED("Nicht gespielt");

    private String name;

    private TotoResult(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }

    /**
     * Parse ein TotoResult aus einem Input String. Valider Input ist: <br\>
     * 0 für WIN_HOME <br\>
     * 1 für WIN_GUEST <br\>
     * 2 für DRAW <br\>
     * 3 für NOT_PLAYED <br\>
     * 
     * @param string
     *            der input String
     * @return das TotoResult, null falls der input invalid war.
     */
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
