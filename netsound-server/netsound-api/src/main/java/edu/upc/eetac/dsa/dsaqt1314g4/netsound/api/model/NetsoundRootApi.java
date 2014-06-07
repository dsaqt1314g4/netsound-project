package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.UserResource;

public class NetsoundRootApi {
	@InjectLinks({
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "create-user", title = "Create user", type = edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.MediaType.NETSOUND_API_USER) })

	private List<Link> links;
	 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
