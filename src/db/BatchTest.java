package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchTest {
	
	private static Connection con = null;
	private static Statement stat = null;
	private static ResultSet rs = null;
	
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String userName = "wilsonUOB";
	private static String passWord = "wilson12345";
	private static String db = "cybersoft";
	private static String url = "jdbc:mysql://localhost/" + db + "?useUnicode=true&characterEncoding=Big5&serverTimezone=CTT";
	
	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userName, passWord);
			stat = con.createStatement();
			
			for (int i = 0; i < 5; i++) {
				String sql = "insert into tb_person "
						+ " ( name, english_name, age, "
						+ " sex, birthday, description) " + " values ('Name "
						+ i + "', 'English Name " + i + "', "
						+ " '17', 'male', current_date(), '') ";
				
				stat.addBatch(sql);
			}
			
			int[] result = stat.executeBatch();
			
			System.out.print("影響的行數分別為：");
			for (int i = 0; i < result.length; i++) {
				System.out.print(result[i] + ", ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (stat != null) stat.close();
			if (con != null) con.close();
		}
	}
}
