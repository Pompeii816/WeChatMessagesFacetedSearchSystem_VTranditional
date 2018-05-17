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
			// 1����֤SQLite���ݿ��ļ���·�����ַ�ΪСд������·��Ϊunix·��
			String thisPath = "C:\\Users\\Pompeii\\Desktop\\Message";
			String str1 = thisPath.substring(0, 1).toLowerCase(); // ֱ�ӽ��ַ�����һ����ĸСд
			String str2 = thisPath.substring(1, thisPath.length());// ��ȡ�ַ����ڶ����Ժ�
			thisPath = str1 + str2;
			String sql = "jdbc:sqlite://" + thisPath.replace('\\', '/') ;// windows && linux������

			// 2������SQLite��JDBC
			Class.forName("org.sqlite.JDBC");

			// ����һ�����ݿ���zieckey.db�����ӣ���������ھ��ڵ�ǰĿ¼���Զ�����
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
