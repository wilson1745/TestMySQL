package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbcmysql {
	private Connection con = null; 			// Database objects 連接object
	private Statement stat = null; 			// 執行,傳入之sql為完整字串
	private ResultSet rs = null;  			// 結果集
	private PreparedStatement pst = null; 	// 執行,傳入之sql為預儲之字申,需要傳入變數之位置 
	
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String userName = "wilsonUOB";
	private String passWord = "wilson12345";
	private String db = "cybersoft";
	private String url = "jdbc:mysql://localhost/" + db + "?useUnicode=true&characterEncoding=Big5&serverTimezone=CTT";

	public Jdbcmysql() {
		try {
			// 註冊driver
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName(driver);
			// 取得connection
			con = DriverManager.getConnection(url, userName, passWord);
			// jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
			// jdbc:mysql://localhost/cmdev?useUnicode=true&characterEncoding=Big5
			// localhost是主機名,test是database名
			// useUnicode=true&characterEncoding=Big5使用的編碼
			// Taiwan:serverTimezone=CTT
			System.out.println("MySQL has been linked successfully.");
		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		} // 有可能會產生sqlexception
		catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
	}

	// 建立table的方式
	// 可以看看Statement的使用方式
	public void createTable() {
		//String createdbSQL = "CREATE TABLE user (" + "id INTEGER" + ", name VARCHAR(20)" + ", passwd VARCHAR(20))";
		String createdbSQL = "CREATE TABLE user (" 
				+ "id INTEGER NOT NULL AUTO_INCREMENT" 
				+ ", name VARCHAR(20)" 
				+ ", passwd VARCHAR(20)" 
				+ ", PRIMARY KEY (id))";

		try {
			stat = con.createStatement();
			stat.executeUpdate(createdbSQL);
			System.out.println("createTable has been linked successfully.");
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// 新增資料
	// 可以看看PrepareStatement的使用方式
	public void insertTable(String name, String passwd) {
		// 先利用?來做標示
		//String insertdbSQL = "INSERT INTO user (id, name, passwd) " + "SELECT IFNULL(max(id),0)+1, ?, ? FROM user";
		String insertdbSQL = "INSERT INTO user" + "(name, passwd) VALUES" + "(?, ?)";

		try {
			pst = con.prepareStatement(insertdbSQL);
			pst.setString(1, name);
			pst.setString(2, passwd);
			pst.executeUpdate();
			System.out.println("insertTable has been linked successfully.");
		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// 刪除Table,
	// 跟建立table很像
	public void dropTable() {
		String dropdbSQL = "DROP TABLE IF EXISTS user";

		try {
			stat = con.createStatement();
			stat.executeUpdate(dropdbSQL);
			System.out.println("dropTable has been linked successfully.");
		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}
	
	public void updateTable() {
		String updatedbSQL = "UPDATE user SET name = 'wilson' WHERE id = 1";
		
		try {
			stat = con.createStatement();
			stat.executeUpdate(updatedbSQL);
			System.out.println("updateTable has been linked successfully.");
		} catch (Exception e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// 查詢資料
	// 可以看看回傳結果集及取得資料方式
	public void SelectTable() {
		String selectSQL = "SELECT * FROM user ";

		try {
			stat = con.createStatement();
			rs = stat.executeQuery(selectSQL);
			System.out.println("ID\t\tName\t\tPASSWORD");
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t\t" + rs.getString("name") + "\t\t" + rs.getString("passwd"));
			}
		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// 完整使用完資料庫後,記得要關閉所有Object
	// 否則在等待Timeout時,可能會有Connection poor的狀況
	private void Close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stat != null) {
				stat.close();
				stat = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}

	public static void main(String[] args) {
		// 測看看是否正常
		Jdbcmysql test = new Jdbcmysql();
		test.dropTable();
		test.createTable();
		test.insertTable("yku", "12356");
		test.insertTable("yku2", "7890");
		test.SelectTable();
		System.out.print("\n");
		test.updateTable();
		test.SelectTable();
	}
}
