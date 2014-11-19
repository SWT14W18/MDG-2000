package de.tudresden.swt14ws18.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TipCollection<T extends Tip> implements Observer {
    private List<T> tips = new ArrayList<>();

    public TipCollection(List<T> tips) {
	this.tips = tips;
	for(T tip : tips)
	    tip.addObserver(this);
    }
    
    public List<T> getTips() {
	return tips;
    }

    @Override
    public void update(Observable o, Object arg) {
	// TODO Auto-generated method stub
	
    }
}
