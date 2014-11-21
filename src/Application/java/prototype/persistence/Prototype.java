package prototype.persistence;

//import java.util.ArrayList;
//import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Prototype{
    
    @GeneratedValue 
    private double value = 0;
	
    public void addingValue(Prototype count) {
	value += count.getCounter();
    }

    private double getCounter() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addTo(double counter) {
	if(counter < 0)
	    return;
	
	Prototype count = new Prototype();
	value += counter;
    }

    public double getValue() {
	return value;
    }
}