package wow.proyectosi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLBasedTest {

	protected static Connection jdbcConnection;
	
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://ndrs.es:3306/si";
	private static final String USER = "siuser";
	private static final String PASS = "sipass";
	
	static {
		try {
			jdbcConnection = createConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection createConnection() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}
	
	public int getLastInsertedId(Statement statement) throws SQLException {
		ResultSet rs = statement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
}
