package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

public class Sting {

	private List<Link> links;
	private String stingid;
	private String content;
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

}
