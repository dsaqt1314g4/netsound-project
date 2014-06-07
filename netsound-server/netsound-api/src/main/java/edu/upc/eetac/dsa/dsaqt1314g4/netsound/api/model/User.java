package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.UserResource;


public class User {
	@InjectLinks({
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "self", title = "Userpage", type = MediaType.NETSOUND_API_USER, method = "getUser", bindings = @Binding(name = "username", value = "${instance.username}")),
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "following-stings", title = "Following Stings", type = MediaType.NETSOUND_API_STING_COLLECTION, method = "getUserFollowingStings", bindings = @Binding(name = "username", value = "${instance.username}")),
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "following", title = "Following", type = MediaType.NETSOUND_API_USER_COLLECTION, method = "getFollowing", bindings = @Binding(name = "username", value = "${instance.username}")),
		@InjectLink(resource = UserResource.class, style = Style.ABSOLUTE, rel = "stings", title = "Stings", type = MediaType.NETSOUND_API_STING_COLLECTION, method = "getUserStings", bindings = @Binding(name = "username", value = "${instance.username}")),
})
	
	
	private List<Link> links;
	private String username;
	private String name;
	private String description;
	private String userpass;
	private String email;
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
