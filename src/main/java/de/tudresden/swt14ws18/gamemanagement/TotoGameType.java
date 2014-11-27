package de.tudresden.swt14ws18.gamemanagement;

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
