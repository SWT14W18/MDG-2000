package lotterie;

public class Bankkonto {
	
	int value;
	String inhaber;
	
	public Bankkonto(String inhaber, int initValue){
		this.inhaber = inhaber;
		this.value = initValue;
	}
	
	public int getCredit(){
		return this.value;
	}
	
	public void setCredit(int amount){
		this.value = amount;
	}

}
