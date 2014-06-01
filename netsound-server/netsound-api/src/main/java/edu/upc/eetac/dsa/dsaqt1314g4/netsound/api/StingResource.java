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

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.StingCollection;

@Path("/stings")
public class StingResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Path("/profile/{profileid}")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserStings(
			@PathParam("profileid") String profileid) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetUserIdStings(), profileid);
		return stings;

	}

	private String buildGetUserIdStings() {

		return "select s.*, u.* from Stings s, StingsRelation r, Users u where s.stingid = r.stingid and u.userid = r.userid and r.userid = ? order by last_modified desc";
	}

	@Path("/profile/{profileid}/following")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserFollowingStings(
			@PathParam("profileid") String profileid) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetFollowingStings(),
				profileid);
		return stings;

	}

	private String buildGetFollowingStings() {

		return "select s.*, u.* from Stings s, StingsRelation r, Follow f, Users u where s.stingid=r.stingid and u.userid=f.followingid and r.userid=f.followingid and f.followerid = ? order by last_modified desc";
	}

	private StingCollection getStingsFromDatabaseByQuery(String Query,
			String profileid) {
		StingCollection stings = new StingCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(Query);
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Sting sting = new Sting();
				sting.setStingid(String.valueOf(rs.getInt("stingid")));
				sting.setUserid(String.valueOf(rs.getInt("userid")));
				sting.setContent(rs.getString("content"));
				sting.setUsername(rs.getString("username"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());
				oldestTimestamp = rs.getTimestamp("last_modified").getTime();
				sting.setLastModified(oldestTimestamp);
				if (first) {
					first = false;
					stings.setNewestTimestamp(sting.getLastModified());
				}
				stings.addSting(sting);
			}
			stings.setOldestTimestamp(oldestTimestamp);
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
		return stings;

	}

	@Path("/{stingid}")
	@GET
	@Produces(MediaType.NETSOUND_API_STING)
	public Sting getSting(@PathParam("stingid") String stingid) {
		Sting sting = new Sting();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetStingById());
			stmt.setInt(1, Integer.valueOf(stingid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				sting.setStingid(String.valueOf(rs.getInt("stingid")));
				sting.setUserid(String.valueOf(rs.getInt("userid")));
				sting.setContent(rs.getString("content"));
				sting.setUsername(rs.getString("username"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());

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

		return sting;
	}

	private String buildGetStingById() {

		return "select s.*, u.* from Stings s, StingsRelation r, users u  where u.userid=r.userid and r.stingid= s.stingid and s.stingid = ?";
	}
}
