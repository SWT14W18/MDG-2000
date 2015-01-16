package Application.java.prototype.persistence;

//import java.util.ArrayList;
//import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import prototype.persistence.PrototypeRepository;

@Entity
public class Prototype {

    @GeneratedValue 
    private double value = 0;
	
    @Deprecated
    public Prototype() {
    };


    public Prototype(double value) {
        this.value = value;
    }
	
    public void addingValue(Prototype count) {
	value += count.getCounter();
    }

    private double getCounter() {
		double value = PrototypeRepository.
		return value;
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