package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.PlaylistCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.SongCollection;

@Path("/playlist")
public class PlaylistResource {
	@Context
	private Application app;
	
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	@Produces(MediaType.NETSOUND_API_PLAYLIST_COLLECTION)
	public PlaylistCollection getPlaylists(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after,
			@QueryParam("name") String name, @QueryParam("style") String style) {
		PlaylistCollection playlists = new PlaylistCollection();
		Connection conn = null;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			boolean updateName = name != null;
			boolean updateStyle = style != null;
			stmt = conn.prepareStatement(buildGetPlaylistQuery(updateFromLast));
			if (updateName) {
				stmt.setString(1, "%" + name + "%");
			} else {
				stmt.setString(1, "%%");
			}

			if (updateStyle) {
				stmt.setString(2, "%" + style + "%");
			} else {
				stmt.setString(2, "%%");
			}

			if (updateFromLast) {
				stmt.setTimestamp(3, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(3, new Timestamp(before));
				else
					stmt.setTimestamp(3, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(4, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Playlist playlist = new Playlist();
				playlist.setPlaylistid(String.valueOf(rs.getInt("playlistid")));
				playlist.setUsername(rs.getString("username"));
				playlist.setPlaylist_name(rs.getString("playlist_name"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("style"));
				playlist.setScore(rs.getString("score"));
				playlists.addPlaylist(playlist);
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return playlists;
	}

	private String buildGetPlaylistQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select * from Playlists where playlist_name like ? and style like ? and last_modified > ? order by last_modified desc";
		else
			return "select * from Playlists where playlist_name like ? and style like ? and last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}

	@Path("/username/{username}")
	@GET
	@Produces(MediaType.NETSOUND_API_PLAYLIST_COLLECTION)
	public PlaylistCollection getPlaylistsByUsername(
			@PathParam("username") String username,
			@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after,
			@QueryParam("name") String name, @QueryParam("style") String style) {
		PlaylistCollection playlists = new PlaylistCollection();
		Connection conn = null;

		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			boolean updateName = name != null;
			boolean updateStyle = style != null;
			stmt = conn
					.prepareStatement(buildGetPlaylistQueryByUsername(updateFromLast));
			stmt.setString(1, username);
			if (updateName) {
				stmt.setString(2, "%" + name + "%");
			} else {
				stmt.setString(2, "%%");
			}

			if (updateStyle) {
				stmt.setString(3, "%" + style + "%");
			} else {
				stmt.setString(3, "%%");
			}

			if (updateFromLast) {
				stmt.setTimestamp(4, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(4, new Timestamp(before));
				else
					stmt.setTimestamp(4, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(5, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Playlist playlist = new Playlist();
				playlist.setPlaylistid(String.valueOf(rs.getInt("playlistid")));
				playlist.setUsername(rs.getString("username"));
				playlist.setPlaylist_name(rs.getString("playlist_name"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("style"));
				playlist.setScore(rs.getString("score"));
				playlists.addPlaylist(playlist);
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return playlists;
	}

	private String buildGetPlaylistQueryByUsername(boolean updateFromLast) {
		if (updateFromLast)
			return "select * from Playlists where username = ? and playlist_name like ? and style like ? and last_modified > ? order by last_modified desc";
		else
			return "select * from Playlists where username = ? and playlist_name like ? and style like ? and last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}

	@Path("/{playlistid}")
	@GET
	@Produces(MediaType.NETSOUND_API_PLAYLIST)
	public Response getPlaylist(@PathParam("playlistid") String playlistid,
			@Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();
		
		Playlist playlist = getPlaylistFromDatabase(playlistid);
		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(playlist.getLastModified()));

		// Verify if it matched with etag available in http request
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(playlist).cacheControl(cc).tag(eTag);

		return rb.build();
	}

	private Playlist getPlaylistFromDatabase(String playlistid) {
		Playlist playlist = new Playlist();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetPlaylistById());
			stmt.setInt(1, Integer.valueOf(playlistid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				playlist.setPlaylistid(String.valueOf(rs.getInt("playlistid")));
				playlist.setUsername(rs.getString("username"));
				playlist.setPlaylist_name(rs.getString("playlist_name"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("style"));
				playlist.setScore(rs.getString("score"));
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return playlist;
	}
	
	@Path("/{playlistid}/songs")
	@GET
	@Produces(MediaType.NETSOUND_API_SONG_COLLECTION)
	public SongCollection getSongs(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		SongCollection songs = new SongCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetPlaylistSongsQuery(updateFromLast));
			
			if (updateFromLast) {
				stmt.setTimestamp(3, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(3, new Timestamp(before));
				else
					stmt.setTimestamp(3, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(4, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Song song = new Song();
				song.setSongid(rs.getString("songid"));
				song.setUsername(rs.getString("username"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setScore(rs.getString("score"));
				song.setSongURL(app.getProperties().get("SongBaseURL") + song.getSongid());
				songs.addSong(song);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return songs;
	}

	private String buildGetPlaylistSongsQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select s.* from Songs s, Playlist_Relation pr where s.songid = pr.songid and pr.playlistid= ?  pr.last_modified > ? order by last_modified desc";
		else
			return "select s.* from Songs s, Playlist_Relation pr where s.songid = pr.songid and pr.playlistid= ?  pr.last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}

	
	private String buildGetPlaylistById() {

		return "select * from Playlists where playlistid= ?";
	}

	@POST
	@Produces(MediaType.NETSOUND_API_PLAYLIST)
	public Playlist uploadPlaylist() {
		Playlist playlist = new Playlist();
		playlist.setPlaylist_name("PruebaPlaylist");
		playlist.setDescription("ewjbfiukydflibalfr");
		playlist.setStyle("Hiphop");
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildPostPlaylist(), Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(2, playlist.getPlaylist_name());
			stmt.setString(3, playlist.getDescription());
			stmt.setString(4, playlist.getStyle());
			stmt.setInt(5, 0);
			stmt.setInt(6, 0);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String playlistid = rs.getString(1);

				playlist = getPlaylistFromDatabase(playlistid);
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		return playlist;
	}
	
	private String buildPostPlaylist() {
		return "insert into Playlists (username, playlist_name, description, style, score, num_votes) value (?,?,?,?,?,?)";
	}
	
	
	
}
