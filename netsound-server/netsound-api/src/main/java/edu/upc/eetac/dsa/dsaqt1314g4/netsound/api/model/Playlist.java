package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.PlaylistResource;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.SongResource;


public class Playlist {
	@InjectLinks({
		@InjectLink(resource = PlaylistResource.class, style = Style.ABSOLUTE, rel = "self", title = "Playlistpage", type = MediaType.NETSOUND_API_PLAYLIST, method = "getPlaylist", bindings = @Binding(name = "playlistid", value = "${instance.playlistid}")),
		@InjectLink(resource = PlaylistResource.class, style = Style.ABSOLUTE, rel = "stings", title = "PlaylistStings", type = MediaType.NETSOUND_API_STING, method = "getPlaylistStings", bindings = @Binding(name = "playlistid", value = "${instance.playlistid}")),
		@InjectLink(resource = PlaylistResource.class, style = Style.ABSOLUTE, rel = "songs", title = "PlaylistSongs", type = MediaType.NETSOUND_API_SONG_COLLECTION, method = "getSongsFromPlaylist", bindings = @Binding(name = "playlistid", value = "${instance.playlistid}")),
		@InjectLink(resource = PlaylistResource.class, style = Style.ABSOLUTE, rel = "upload-song", title = "Playlist Upload Song", type = MediaType.NETSOUND_API_SONG, method = "uploadSongInToPlaylist", bindings = @Binding(name = "playlistid", value = "${instance.playlistid}")),
		@InjectLink(resource = PlaylistResource.class, style = Style.ABSOLUTE, rel = "delete-playlist", title = "Delete Playlist", type = MediaType.NETSOUND_API_PLAYLIST, method = "deletePlaylist", bindings = @Binding(name = "playlistid", value = "${instance.playlistid}")),
			
	})
	
	private List<Link> links;
	private String playlistid;
	private String username;
	private String playlist_name;
	private String description;
	private String style;
	private long lastModified;
	private String score;
	private String num_votes;
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getPlaylistid() {
		return playlistid;
	}
	public void setPlaylistid(String playlistid) {
		this.playlistid = playlistid;
	}
	public String getPlaylist_name() {
		return playlist_name;
	}
	public void setPlaylist_name(String playlist_name) {
		this.playlist_name = playlist_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNum_votes() {
		return num_votes;
	}
	public void setNum_votes(String num_votes) {
		this.num_votes = num_votes;
	}
	
}
