package DataProcessModel;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
	private final static String driverString = "";
	private final static String connectionString = "";
	private final static String username = "";
	private final static String password = "";
	
	public static Connection getConnection() throws Exception {
        Connection connection = null;
        try {
            Class.forName(driverString);
            connection = DriverManager.getConnection(connectionString,username,password);
        } catch (Exception e) {
            throw e;
        }
        return connection;
    }
    public static void closeStatement(Statement statement) throws Exception {
    	statement.close();
    }
    public static void closePreparedStatement(PreparedStatement pStatement)
            throws Exception {
        pStatement.close();
    }
    public static void closeResultSet(ResultSet resultSet) throws Exception {
        resultSet.close();
    }
    public static void closeConnection(Connection connection) throws Exception {
        connection.close();
    }
    
    /*test
    public static void main(String[] args) throws Exception {
    	
		Connection con = DBUtil.getConnection();
    	System.out.println(con);
    }*/
}
