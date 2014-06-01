package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.PlaylistCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.UserCollection;

@Path("/playlist")
public class PlaylistResource {
	

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Path("/{playlistid}")
	@GET
	@Produces(MediaType.NETSOUND_API_PLAYLIST)
	public Playlist getPlaylist(@PathParam("playlistid") String profileid) {
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
			stmt = conn.prepareStatement(buildGetUserById());
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				playlist.setPlaylistid(String.valueOf(rs.getInt("playlistid")));
				playlist.setUserid(String.valueOf(rs.getInt("userid")));
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

	private String buildGetUserById() {

		return "select * from Playlist where userid = ?";
	}

	@Path("/{playlist}/playlistid")
	@GET
	@Produces(MediaType.NETSOUND_API_PLAYLIST_COLLECTION)
	public UserCollection getFollowing(@PathParam("profileid") String profileid) {
		UserCollection following = new UserCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetFollowingById());
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Playlist playlist = new Playlist();
				playlist.setPlaylistid(String.valueOf(rs.getInt("playlistid")));
				playlist.setUserid(String.valueOf(rs.getInt("userid")));
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

		return following ;
	}

	private String buildGetFollowingById() {
		return "select p.* from Playlist u where p.playlistid = ?";
	}


	@POST
	@Consumes(MediaType.NETSOUND_API_PLAYLIST)
	@Produces(MediaType.NETSOUND_API_PLAYLIST)
/*	public Playlist createPlaylist(Playlist playlist) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

//		PreparedStatement stmt = null;
//		PreparedStatement stmt2 = null;
//		try {
//			if(playlist.getUserpass() == null){
//				throw new BadRequestException("Password Can't be null");
//			}
//			stmt = conn.prepareStatement(buildCreateUser(),
//					Statement.RETURN_GENERATED_KEYS);
//			stmt.setString(1, playlist.getUsername());
//			stmt.setString(2, playlist.getName());
//			if (playlist.getDescription() == null) {
//				playlist.setDescription("I'm always tired, because I'm a superhero at night");
//			}
//			stmt.setString(3, playlist.getDescription());
//			stmt.executeUpdate();
//
//
//			
//			ResultSet rs = stmt.getGeneratedKeys();
//			if (rs.next()) {
//				String userid = String.valueOf(rs.getInt(1));
//				playlist = getUser(userid);
//			} else {
//
//			}
//
//		} catch (SQLException e) {
//			throw new ServerErrorException(e.getMessage(),
//					Response.Status.INTERNAL_SERVER_ERROR);
//		} finally {
//			try {
//				if (stmt != null)
//					stmt.close();
//				conn.close();
//			} catch (SQLException e) {
//			}
//		}
//
//		return playlist;

	}

	/*private String buildCreateRealmUser() {
		
		return "insert into realmdb.users (username, userpass) values (?, MD5(?))" ;
	}*/

	private String buildCreateUser() {

		return "insert into Users (username, name, description) values (?, ?, ?);";
	}

}

