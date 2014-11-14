package lotterie;

import org.hibernate.validator.constraints.NotEmpty;

public class Registration {
	
	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}")
	private String name;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}")
	private String password;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
