package prototype.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Prototype{
    
    @Id
    @GeneratedValue
    private long id; 
    private double value = 0;
	
	
    @OneToMany
    private List<Prototype> prototypeList = new ArrayList<>();

    public void addingValue(Prototype count) {
	value += count.getCounter();
	prototypeList.add(count);
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
	prototypeList.add(count);
    }

    public double getValue() {
	return value;
    }

    public List<Prototype> getprototypeList() {
	return prototypeList;
    }

    public boolean hasValue(double value) {
	return value >= value;
    }
}