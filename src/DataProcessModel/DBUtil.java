package DataProcessModel;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {

	public static Connection getConnection() throws Exception {
		Connection conn = null;
		try {
			// 1，保证SQLite数据库文件的路径首字符为小写，并且路径为unix路径
			String thisPath = "C:\\Users\\Pompeii\\Desktop\\Message";
			String str1 = thisPath.substring(0, 1).toLowerCase(); // 直接将字符串第一个字母小写
			String str2 = thisPath.substring(1, thisPath.length());// 截取字符串第二个以后
			thisPath = str1 + str2;
			String sql = "jdbc:sqlite://" + thisPath.replace('\\', '/') ;// windows && linux都适用

			// 2，连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");

			// 建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下自动创建
			conn = DriverManager.getConnection(sql);
		} catch (Exception e) {
			throw e;
		}
		return conn;
	}

	public static void closeStatement(Statement statement) throws Exception {
		statement.close();
	}

	public static void closePreparedStatement(PreparedStatement pStatement) throws Exception {
		pStatement.close();
	}

	public static void closeResultSet(ResultSet resultSet) throws Exception {
		resultSet.close();
	}

	public static void closeConnection(Connection connection) throws Exception {
		connection.close();
	}

	/*
	public static void main(String[] args) throws Exception {
		Connection con = DBUtil.getConnection();
		System.out.println(con);
	}*/
}
