package de.tudresden.swt14ws18.gamemanagement;

/**
 * Repräsentiert die Typen von Spielen, die die Lotterie anbietet.
 */
public enum GameType {
    LOTTO("Lotto"),
    TOTO("Toto");

    private final String name;

    private GameType(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return name;
    }
}
