import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final String DB_URL = "jdbc:mysql://localhost/jklaptop";
	final String USER = "root";
	final String PASS = "";
	private Statement stmt;
	PreparedStatement ps;
	Connection conn;
	
	public Connect(){
		try {

			
			Class.forName(JDBC_DRIVER);
			conn=DriverManager.getConnection(DB_URL, USER, PASS);
			stmt=conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String query) {
		ResultSet res = null;
		try {
			res=stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void executeUpdate(String query) {
		ResultSet res = null;
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//prepared statement
	public void insertAccount(String id, String username, String email, String password, String gender, String address, String role) {
		try {
			ps = conn.prepareStatement("INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, id);
			ps.setString(2, username);
			ps.setString(3, email);
			ps.setString(4, password);
			ps.setString(5, gender);
			ps.setString(6, address);
			ps.setString(7, role);
			ps.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}