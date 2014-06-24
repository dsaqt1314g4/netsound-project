package edu.upc.eetac.dsa.dsaqt1314g4.netsound.model;

import java.io.Serializable;


public class User implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private List<Link> links;
	private String username;
	private String name;
	private String description;
	private String token;
	private String email;
	private long date_create;
	private boolean myFollower = false;
	
		
	public User(String username, String name, String description,
			String token, String email, long date_create) {
		super();
		this.username = username;
		this.name = name;
		this.description = description;
		this.token = token;
		this.email = email;
		this.date_create = date_create;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getDate_create() {
		return date_create;
	}
	public void setDate_create(long date_create) {
		this.date_create = date_create;
	}
	public boolean isMyFollower() {
		return myFollower;
	}
	public void setMyFollower(boolean myFollower) {
		this.myFollower = myFollower;
	}
	
	
}
