package prototype;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import prototype.persistence.Prototype;
import prototype.persistence.PrototypeRepository;


@Component
public class PrototypeDataInitializer implements DataInitializer{

	private final PrototypeRepository prototypeRepository;
	
	@Autowired
	public MainDataInitializer(PrototypeRepository prototypeRepository){
		Assert.notNull(prototypeRepository, "PrototypeRepository must not be null!");
		this.prototypeRepository = prototypeRepository;
	}
	
	@Override
	public void initialize() {
		initializeUsers(prototypeRepository);
	}
}