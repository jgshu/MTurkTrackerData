import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SpecificationExtraction {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Connection conn=null;
		String SQLUrl="jdbc:mysql://localhost:3306/mturk?"+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		String selectSQL;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("成功加载MySQL驱动程序");
		try {
			PrintWriter writer = new PrintWriter(new FileWriter("specification_4k.txt"));
			conn=DriverManager.getConnection(SQLUrl);
			selectSQL="select keywords, description from uniquetask limit 4000";
			PreparedStatement selectpstmt=conn.prepareStatement(selectSQL);
			ResultSet rs=selectpstmt.executeQuery();
			int num_key=0;
			while(rs.next()){
				String keywords=rs.getString("keywords");
				String[] keyowrdSet=keywords.split(",");
				num_key+=keyowrdSet.length;
				String description=rs.getString("description");				
				writer.println(description);
					 
			}
			writer.close();
			rs.close();
			selectpstmt.close();
			conn.close();
			System.out.println(num_key);
			System.out.println("finished");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
