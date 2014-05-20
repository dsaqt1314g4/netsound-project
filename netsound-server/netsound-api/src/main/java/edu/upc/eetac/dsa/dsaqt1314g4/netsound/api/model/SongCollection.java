package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;


public class SongCollection {
	private List<Link> links;
	private List<Song> songs;
	private long newestTimestamp;
	private long oldestTimestamp;
	
	public SongCollection() {
		super();
		songs = new ArrayList<>();
	}
	
	public void addSongg(Song song) {
		songs.add(song);
	}
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
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
	
	
}
