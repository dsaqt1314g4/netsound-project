package edu.upc.eetac.dsa.dsaqt1314g4.netsound.model;

public class Sting {

	//private List<Link> links;
	private String stingid;
	private String content;
	private String username;
	private long lastModified;
	
	public Sting(String stingid, String content, String username,
			long lastModified) {
		super();
		this.stingid = stingid;
		this.content = content;
		this.username = username;
		this.lastModified = lastModified;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}	
	
	
}
