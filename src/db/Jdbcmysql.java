package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbcmysql {
	private Connection con = null; 			// Database objects �s��object
	private Statement stat = null; 			// ����,�ǤJ��sql������r��
	private ResultSet rs = null;  			// ���G��
	private PreparedStatement pst = null; 	// ����,�ǤJ��sql���w�x���r��,�ݭn�ǤJ�ܼƤ���m 
	
	private String userName = "wilsonUOB";
	private String passWord = "wilson12345";

	public Jdbcmysql() {
		try {
			// ���Udriver
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			// ���oconnection
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost/cmdev?useUnicode=true&characterEncoding=Big5&serverTimezone=CTT", 
					userName, 
					passWord
					);
			// jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
			// jdbc:mysql://localhost/cmdev?useUnicode=true&characterEncoding=Big5
			// localhost�O�D���W,test�Odatabase�W
			// useUnicode=true&characterEncoding=Big5�ϥΪ��s�X
			// Taiwan:serverTimezone=CTT
			System.out.println("MySQL has been linked successfully.");
		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		} // ���i��|����sqlexception
		catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
	}

	// �إ�table���覡
	// �i�H�ݬ�Statement���ϥΤ覡
	public void createTable() {
		String createdbSQL = "CREATE TABLE User (" + "id INTEGER" + ", name VARCHAR(20)" + ", passwd VARCHAR(20))";

		try {
			stat = con.createStatement();
			stat.executeUpdate(createdbSQL);
		} catch (SQLException e) {
			System.out.println("CreateDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// �s�W���
	// �i�H�ݬ�PrepareStatement���ϥΤ覡
	public void insertTable(String name, String passwd) {
		// ���Q��?�Ӱ��Х�
		String insertdbSQL = "INSERT INTO User(id, name, passwd) " + "SELECT IFNULL(max(id),0)+1, ?, ? FROM User";

		try {
			pst = con.prepareStatement(insertdbSQL);
			pst.setString(1, name);
			pst.setString(2, passwd);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("InsertDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// �R��Table,
	// ��إ�table�ܹ�
	public void dropTable() {
		String dropdbSQL = "DROP TABLE IF EXISTS user";

		try {
			stat = con.createStatement();
			stat.executeUpdate(dropdbSQL);
		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}
	
	public void updateTable() {
		String updatedbSQL = "UPDATE User SET name = 'wilson' WHERE id = 1";
		
		try {
			stat = con.createStatement();
			stat.executeUpdate(updatedbSQL);
		} catch (Exception e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	// �d�߸��
	// �i�H�ݬݦ^�ǵ��G���Ψ��o��Ƥ覡
	public void SelectTable() {
		String selectSQL = "SELECT * FROM User ";

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

	// ����ϥΧ���Ʈw��,�O�o�n�����Ҧ�Object
	// �_�h�b����Timeout��,�i��|��Connection poor�����p
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
		// ���ݬݬO�_���`
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
