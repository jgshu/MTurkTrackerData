import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class plainIndexGen {
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
		FileWriter writer=new FileWriter("plainIndex10.txt",true);
		try {			 
			Set<String> set=new HashSet<String>();
			conn=DriverManager.getConnection(SQLUrl);
			selectSQL="select id, keywords from uniqueTask limit 100000";
			PreparedStatement pstmt=conn.prepareStatement(selectSQL);
			ResultSet rs=pstmt.executeQuery();
			int rowCount=0;
			while(rs.next()){
				String id=rs.getString("id");
				String keywords=rs.getString("keywords");	
				String pieceTask=id+":"+keywords;
				writer.write(pieceTask);
				writer.write("\r\n");	
				rowCount++;
				}  
			writer.close();							
			rs.close();
			conn.close();
			System.out.println("write done "+rowCount);	
			
			        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
