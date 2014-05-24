package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.UserCollection;

@Path("/profile")
public class UserResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Path("/{profileid}")
	@GET
	@Produces(MediaType.NETSOUND_API_USER)
	public User getUser(@PathParam("profileid") String profileid) {
		User user = new User();

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
				user.setUserid(String.valueOf(rs.getInt("userid")));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
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

		return user;
	}

	private String buildGetUserById() {

		return "select * from Users where userid = ?";
	}

	@Path("/{profileid}/following")
	@GET
	@Produces(MediaType.NETSOUND_API_USER_COLLECTION)
	public UserCollection getFollowing(@PathParam("profileid") String profileid){
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
				User user = new User();
				user.setUserid(String.valueOf(rs.getInt("userid")));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
				following.addUser(user);
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

		return following;
	}

	private String buildGetFollowingById() {
		// TODO Auto-generated method stub
		return "select u.* from Users u, follow f where f.followingid = u.userid and f.followerid = ?";
	}
	
	
}
