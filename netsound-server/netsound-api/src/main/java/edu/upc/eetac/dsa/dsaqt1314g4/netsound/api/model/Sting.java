package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.UserResource;

public class Sting {
	@InjectLinks({
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "user", title = "Userpage", type = MediaType.NETSOUND_API_USER, method = "getUser", bindings = @Binding(name = "profileid", value = "${instance.userid}")) })
	

	private List<Link> links;
	private String stingid;
	private String content;
	private String username;
	private long lastModified;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public String getStingid() {
		return stingid;
	}

	public void setStingid(String stingid) {
		this.stingid = stingid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
