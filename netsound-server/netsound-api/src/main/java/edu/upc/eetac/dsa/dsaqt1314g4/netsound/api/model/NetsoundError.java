package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

public class NetsoundError{
private int status;
private String message;

public NetsoundError() {
	super();
}

public NetsoundError(int status, String message) {
	super();
	this.status = status;
	this.message = message;
}

public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}


}
