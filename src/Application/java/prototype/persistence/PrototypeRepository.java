package Application.java.prototype.persistence;

import java.io.Serializable;
import org.springframework.*;
import org.springframework.data.repository.CrudRepository;

public interface PrototypeRepository extends CrudRepository<String, Long> {
	
}