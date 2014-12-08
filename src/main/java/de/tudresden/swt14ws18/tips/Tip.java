package de.tudresden.swt14ws18.tips;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import de.tudresden.swt14ws18.gamemanagement.Game;

@MappedSuperclass
public abstract class Tip/* extends Observable */{

    @GeneratedValue
    @Id
    public long id;

    private boolean valid = true;

    public Tip() {
    }

    public boolean isValid() {
        return valid;
    }

    public abstract String toCustomString();

    public void invalidate(boolean valid) {
        this.valid = valid;
    }

    public abstract Game getGame();

    public abstract double getInput();

    public abstract double getWinAmount();

    public boolean isFinished() {
        return getGame().isFinished();
    }

}
