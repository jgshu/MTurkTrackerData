import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class UniqueTaskExtraction {

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
			conn=DriverManager.getConnection(SQLUrl);
			selectSQL="select id,keywords,description,requesterId from task";
			String insertSQL="insert ignore into uniqueTask(id,keywords,description,requesterId) values(?,?,?,?)";
			PreparedStatement insertpstmt = (PreparedStatement) conn.prepareStatement(insertSQL);
			PreparedStatement selectpstmt=conn.prepareStatement(selectSQL);
			ResultSet rs=selectpstmt.executeQuery();
			while(rs.next()){
				String id=rs.getString("id");
				String keywords=rs.getString("keywords").toLowerCase();	
				if(keywords.equals(""))
					continue;
				String[] keyowrdSet=keywords.split(",");
				if(keyowrdSet.length>10)
					continue;
				String post_keywords="";
				for(int i=0;i<keyowrdSet.length-1;i++)
				{
					post_keywords+=keyowrdSet[i].trim()+",";
				}
				post_keywords+=keyowrdSet[keyowrdSet.length-1].trim();
				String description=rs.getString("description");		
				String requesterId=rs.getString("requesterId");	
				
				insertpstmt.setString(1,id);
				insertpstmt.setString(2,post_keywords);
				insertpstmt.setString(3,description);
				insertpstmt.setString(4,requesterId);
				int result=insertpstmt.executeUpdate();
	        	if(result==-1){
	        		System.out.println("insert error!");
	        		continue;
	        	}	 
			}
			rs.close();
			insertpstmt.close();
			selectpstmt.close();
			conn.close();
			System.out.println("finished");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
