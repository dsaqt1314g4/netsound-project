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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.PlaylistCollection;

@Path("/playlist")
public class PlaylistResource {
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
				playlist.setPlaylist_name(rs.getString("playlistname"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("description"));
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
				playlist.setPlaylist_name(rs.getString("playlistname"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("description"));
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
	public Playlist getPlaylist(@PathParam("playlistid") String playlistid) {
		Playlist playlist = new Playlist();
		playlist = getPlaylistFromDatabase(playlistid);
		return playlist;
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
				playlist.setPlaylist_name(rs.getString("playlistname"));
				playlist.setDescription(rs.getString("description"));
				playlist.setStyle(rs.getString("description"));
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
	
	private String buildGetPlaylistById() {

		return "select * from Playlist where playlistid= ?";
	}

	@POST
	@Produces(MediaType.NETSOUND_API_PLAYLIST)
	public Playlist uploadPlaylist() {
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
			stmt = conn.prepareStatement(buildPostPlaylist(), Statement.RETURN_GENERATED_KEYS);
			stmt.setString(2, "alejandro.jimenez");// security.getUserPrincipal().getName());
			stmt.setString(3, playlist.getPlaylist_name());
			stmt.setString(5, playlist.getDescription());
			stmt.setString(6, playlist.getStyle());
			stmt.setInt(7, 0);
			stmt.setInt(8, 0);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String playlistid = rs.getString(0);

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
		return "insert into Playlist (username, playlist_name, description, style, score, num_votes) value (?,?,?,?,?,?)";
	}
	
}
