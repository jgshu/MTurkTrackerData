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


public class KeywordSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
			Set<String> set=new HashSet<String>();
			conn=DriverManager.getConnection(SQLUrl);
			selectSQL="select Keywords from uniqueTask";
			PreparedStatement pstmt=conn.prepareStatement(selectSQL);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				String keywords=rs.getString("keywords");
				String[] keywordSet=keywords.split(",");
				for(int i=0;i<keywordSet.length;i++)
				{
					set.add(keywordSet[i].toLowerCase());
				}
				
			}
			System.out.println(set.size());
			rs.close();
			conn.close();
			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter("UniqueKeywords.txt"));
				Iterator<String> ite=set.iterator();
				while(ite.hasNext()){
					writer.println(ite.next().trim());
				}
				writer.close();
				System.out.println("write done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}          	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
