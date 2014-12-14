package de.tudresden.swt14ws18.gamemanagement;

/**
 * Repäsentiert die verschiedenen Ligen, die die Lotterie für das Fußball-Toto unterstützt.
 */
public enum TotoGameType {
    POKAL("DFB Pokal"),
    BUNDESLIGA1("1. Bundesliga"),
    BUNDESLIGA2("2. Bundesliga");

    private String name;

    private TotoGameType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
