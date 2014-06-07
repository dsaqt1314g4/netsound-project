package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.NetsoundRootAPIResource;

public class NetsoundRootApi {
	@InjectLinks({
		@InjectLink(resource = NetsoundRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Netsound Root API", method = "getRootAPI"),
		 })
	
	private List<Link> links;
	 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
