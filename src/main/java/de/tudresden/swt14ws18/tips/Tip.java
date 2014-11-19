package de.tudresden.swt14ws18.tips;

import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;

import de.tudresden.swt14ws18.Lotterie;

public abstract class Tip extends Observable implements Observer {

    private Lotterie lotterie;

    public Tip() {
    }

    @Autowired
    protected void setLotterie(Lotterie lotterie) {
	this.lotterie = lotterie;
    }

    public Lotterie getLotterie() {
	return lotterie;
    }

}
