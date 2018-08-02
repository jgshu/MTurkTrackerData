import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


public class BuildIntIndex {

    public static String[] readFileByLines(String fileName, int num) {
    	String[] keywordSet=new String[num];
    	
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
          //  System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null&& line<num) {
                // 显示行号
              //  System.out.println("line " + line + ": " + tempString);
            	keywordSet[line]=tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return keywordSet;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		String SQLUrl="jdbc:mysql://localhost:3306/mturk?"+ "user=root&password=&useUnicode=true&characterEncoding=UTF8";
		String selectSQL;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("成功加载MySQL驱动程序");
		try 
		{
			conn=DriverManager.getConnection(SQLUrl);	
			String insertSQL="insert ignore into worker(keywords) values(?)";
			PreparedStatement insertpstmt = (PreparedStatement) conn.prepareStatement(insertSQL);
			String fileName = "UniqueKeywords.txt";
			int num=4744;
			String[] keywordSet=readFileByLines(fileName, 4744);
			for(int i=0;i<10000;i++)
			{
				Random rand=new Random();
				int keywordsNum=rand.nextInt(10)+1;
				String interest="";
				for(int j=0;j<keywordsNum;j++)
				{
					Random rand1=new Random();
					int index=rand1.nextInt(4744);
					interest+=keywordSet[index]+",";
				}
				interest=interest.substring(0,interest.length()-1);
				insertpstmt.setString(1, interest);
				int result=insertpstmt.executeUpdate();
	        	if(result==-1){
	        		System.out.println("insert error!");
	        		continue;
	        	}	
			}	
			insertpstmt.close();		
			conn.close();
			System.out.println("finished");
			
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}