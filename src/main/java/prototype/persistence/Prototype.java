package prototype.persistence;

import java.util.ArayList;
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
    private List<SafedEntity> prototypeList = new ArayList<>();

    public void addingValue(SafedEntity count) {
	value += count.getCounter();
	prototypeList.add(count);
    }

    public void addTo(double counter) {
	if(counter < 0)
	    return;
	
	SafedEntity count = new SafedEntity(null, this, counter);
	value += counter;
	prototypeList.add(count);
    }

    public double getValue() {
	return value;
    }

    public List<SafedEntity> getprototypeList() {
	return prototypeList;
    }

    public boolean hasValue(double value) {
	return value >= value;
    }
}