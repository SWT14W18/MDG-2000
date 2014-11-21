package prototype;

import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

//import prototype.persistence.Prototype;
import prototype.persistence.PrototypeRepository;


@Component
public class PrototypeDataInitializer implements DataInitializer{

	private PrototypeRepository prototypeRepository;
	
	@Autowired
	public PrototypeDataInitializer(PrototypeRepository prototypeRepository){
		Assert.notNull(prototypeRepository, "PrototypeRepository must not be null!");
		this.prototypeRepository = prototypeRepository;
	}
	
	@Override
	public void initialize() {
		initializeUsers(prototypeRepository);
	}

	private void initializeUsers(PrototypeRepository prototypeRepository2) {
		// TODO Auto-generated method stub
		
	}
}