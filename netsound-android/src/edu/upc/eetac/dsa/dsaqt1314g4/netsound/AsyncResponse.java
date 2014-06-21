package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;

public interface AsyncResponse {
	public void goToHome(User user);
	public void printError(String error);
	public void printStings(List<Sting> stingList);
}
