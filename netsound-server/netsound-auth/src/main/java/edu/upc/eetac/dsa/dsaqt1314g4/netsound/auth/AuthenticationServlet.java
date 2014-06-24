package edu.upc.eetac.dsa.dsaqt1314g4.netsound.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONException;
import org.json.JSONObject; 


/**
 * Servlet implementation class AuthenticationServlet
 */
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public AuthenticationServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Context envContext = null;
		try {
			envContext = new InitialContext();
			Context initContext = (Context) envContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) initContext.lookup("jdbc/netsounddb");
			Connection con = ds.getConnection();
			PreparedStatement stmt = null;
			String query = "select * from users where username = ? and userpass = MD5(?);";
			stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			JSONObject json = new JSONObject();
			if (rs.next()) {
				System.out.println("True");
				json.put("success", true);
			}else{
				json.put("success", false);
			}
			response.setHeader("Content-Type", "application/json");
			PrintWriter writer = response.getWriter();
			writer.println(json.toString());
			writer.flush();
			writer.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

}
