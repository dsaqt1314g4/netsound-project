package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

public class StingCollection {
	private List<Link> links;
	private List<Sting> stings;
	private long newestTimestamp;
	private long oldestTimestamp;

	public StingCollection() {
		super();
		stings = new ArrayList<>();
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<Sting> getStings() {
		return stings;
	}

	public void setStings(List<Sting> stings) {
		this.stings = stings;
	}

	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}
	
	public void addSting(Sting sting){
		stings.add(sting);
	}
}
