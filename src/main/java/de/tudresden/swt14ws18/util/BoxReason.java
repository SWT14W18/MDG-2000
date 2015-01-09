package de.tudresden.swt14ws18.util;

public enum BoxReason {
	TIPPCHANGESUCCESS("tippChangeSuccess"),
	TIPPCHANGEERROR("tippChangeError"),
	TIPPDELETESUCCESS("tippDeleteSuccess"),
	TIPPDELETEERROR("tippDeleteError");
	
	private String type;
	
	private BoxReason(String type){
		this.type = type;
	}
	
//	@Override
//	public String toString(){
//		return type;
//	}
	
	public static BoxReason parseString(String type){
		switch(type){
		case "tippChangeSuccess":
			return TIPPCHANGESUCCESS;
		case "tippChangeErrors":
			return TIPPCHANGEERROR;
		case "tippDeleteSuccess":
			return TIPPDELETESUCCESS;
		case "tippDeleteError":
			return TIPPDELETEERROR;
		default:
			return null;
		}	
	}
	
	
	
}
