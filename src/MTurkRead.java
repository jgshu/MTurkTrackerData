import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MTurkRead {
	public static String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
       // String str="";
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    }  
	public static void main(String[] args) {
		Connection conn=null;
		String SQLUrl="jdbc:mysql://localhost:3306/mturk?"+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		String insertSQL;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动程序");
			conn=DriverManager.getConnection(SQLUrl);
			Statement stmt=conn.createStatement();
			PreparedStatement pstmt;
			insertSQL="insert ignore into task(id,title,keywords,reward,description,requesterId,requesterName,timeAllotted) values(?,?,?,?,?,?,?,?)";
			pstmt = (PreparedStatement) conn.prepareStatement(insertSQL);
						
			String ApiUrl="https://crowd-power.appspot.com/_ah/api/mturk/v1/hitgroup/search";
	        String Content = loadJson(ApiUrl);	        
	        JSONObject ContentJson =new JSONObject(Content);
	        JSONArray ContentItems = ContentJson.getJSONArray("items");
	        for(int i = 0 ; i < ContentItems.length() ; i++)  
	        { 
	        	JSONObject TaskObj = ContentItems.getJSONObject(i);  
	        	String keywords=TaskObj.getString("keywords");
	        	//keywords="record, voice, sound, audio data, phrase, speech recording";	        
	        	if (!keywords.equals("")){
	        		String[] keywordSet=keywords.split(",");
		        	
					for(int j=0;j<keywordSet.length;j++)
					{
						String keywordStr=keywordSet[j].trim();
						System.out.println(keywordStr);
						//keywordStr="matching";
						String KeywordApiUrl="https://crowd-power.appspot.com/_ah/api/mturk/v1/hitgroup/search?keyword="+keywordStr;
						String hitGroupContent = loadJson(KeywordApiUrl);	
						try {
			        		JSONObject hitGroupjson =new JSONObject(hitGroupContent);
				 	        JSONArray hitGroupitems = hitGroupjson.getJSONArray("items");  
				 	        for(int k = 0 ; k < hitGroupitems.length() ; k++)  {	 	        
				 	        	JSONObject hitGroupobj = hitGroupitems.getJSONObject(k);			 	        	
				 	        	pstmt.setString(1,hitGroupobj.getString("id"));
						        pstmt.setString(2,hitGroupobj.getString("title"));
						        pstmt.setString(3,hitGroupobj.getString("keywords"));
						        pstmt.setInt(4,hitGroupobj.getInt("reward"));
						        pstmt.setString(5,hitGroupobj.getString("description"));
						        pstmt.setString(6,hitGroupobj.getString("requesterId"));					        
						        pstmt.setString(7,hitGroupobj.getString("requesterName"));
						        pstmt.setInt(8,hitGroupobj.getInt("timeAllotted"));
						        	
						        int result=pstmt.executeUpdate();
						        if(result==-1){
						        	System.out.println(hitGroupobj.getString("id")+"insert error!");
						        	continue;
						        }	 
				 	  
				 	        }	        		 	       	       		        		                		        		
			 	        }catch(JSONException e){
			 	        	e.printStackTrace();
			 	        	System.out.println(i+KeywordApiUrl);
			 	        }	
			 	        
						
					}
	        	}
	        	System.out.println(i);
	        	
	        }
	        
	        
	        pstmt.close();
	        conn.close();
	        System.out.println("finished");
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		
		
	}
}
