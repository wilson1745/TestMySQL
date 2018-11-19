package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchTest {
	
	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String userName = "wilsonUOB";
	private final String passWord = "wilson12345";
	private final String db = "cybersoft";
	private final String url = "jdbc:mysql://localhost/" + db + "?useUnicode=true&characterEncoding=Big5&serverTimezone=CTT";
	
	public BatchTest() {
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
			Close();
		}
	}
	
	private void Close() {
		try {
			if (rs != null) rs.close();
			if (stat != null) stat.close();
			if (con != null) con.close();
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}
	
	public static void main(String[] args) throws SQLException {
		BatchTest bt = new BatchTest();
	}
}
