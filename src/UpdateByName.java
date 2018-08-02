import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateByName {
	public static Set<String> readFileByLines(String fileName) {
    	Set<String> preSet=new HashSet<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
          //  System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
              //  System.out.println("line " + line + ": " + tempString);
            	String[] keywordUnitSet=tempString.split(" "); // split space
            	for(int i=0;i<keywordUnitSet.length;i++){
            		preSet.add(keywordUnitSet[i].trim().toLowerCase());     
            	}          	        
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
        return preSet;
    }
	
	public static void writeFileByLines(String fileName,Set<String> newSet) throws IOException {
		FileWriter writer=new FileWriter(fileName,true);
		Iterator<String> ite=newSet.iterator();
		while(ite.hasNext()){
			String keywordStr=ite.next();
			writer.write(keywordStr);
			writer.write("\r\n");	
		}
		writer.close();	
		System.out.println("new keyword set write done"+newSet.size());	
	}
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
public static void main(String[] args) throws IOException {
		
		String preFileName="previousNames.txt";
		Set<String> preSet=readFileByLines(preFileName);
		System.out.println("preSet size "+preSet.size());
		
		
		
		Connection conn=null;
		String SQLUrl="jdbc:mysql://localhost:3306/mturk?"+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		String insertSQL;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动程序");
			conn=DriverManager.getConnection(SQLUrl);					
			insertSQL="insert ignore into task(id,title,keywords,reward,description,requesterId,requesterName,timeAllotted) values(?,?,?,?,?,?,?,?)";			
			PreparedStatement insertpstmt = (PreparedStatement) conn.prepareStatement(insertSQL);		
			/*** read requesterName from sql database***/
			
			String selectSQL="select requesterName from task";
			PreparedStatement selectpstmt=conn.prepareStatement(selectSQL);
			ResultSet rs=selectpstmt.executeQuery();
			Set<String> newSet=new HashSet<String>();
			while(rs.next()){
				String name=rs.getString("requesterName");	
				//newSet.add(name);			
				String[] nameKeywordSet=name.split(" ");
				for(int j=0;j<nameKeywordSet.length;j++){
					newSet.add(nameKeywordSet[j].replaceAll("[\\pP\\p{Punct}]","").trim().toLowerCase());
				}
			}
			selectpstmt.close();
			
			
			
			System.out.println("new set berfore remove "+newSet.size());
			newSet.removeAll(preSet);
			System.out.println("new set after remove "+newSet.size());
		
	
			int count=0;
			
			Iterator<String> ite=newSet.iterator();
			Set<String> writeSet=new HashSet<String>();
			while(ite.hasNext()){
				if(count%1000==0){
					writeFileByLines(preFileName,writeSet);	//update keyword set
					writeSet.clear();
				}
				String rName=ite.next();
				if (rName.equals("")){
					System.out.println((count++)+":"+rName);
					continue;
				}		
				writeSet.add(rName);
				//rName=rName.trim().replace(" ","+");
				//rName="Jordan Kraft".replace(" ","+"); //example
				String nameApiUrl="https://crowd-power.appspot.com/_ah/api/mturk/v1/hitgroup/search?requesterName="+rName;
				String hitGroupContent = loadJson(nameApiUrl);	
				try {
					if(hitGroupContent.contentEquals("{ }")){				
						System.out.println((count++)+":"+rName +" not found");
						continue;
					}
						
					JSONObject hitGroupjson =new JSONObject(hitGroupContent);
			        JSONArray hitGroupitems = hitGroupjson.getJSONArray("items");  
			 	    for(int k = 0 ; k < hitGroupitems.length() ; k++){
			 	    	JSONObject hitGroupobj = hitGroupitems.getJSONObject(k);			 	        	
			 	    	insertpstmt.setString(1,hitGroupobj.getString("id"));
			 	    	insertpstmt.setString(2,hitGroupobj.getString("title"));
			 	    	insertpstmt.setString(3,hitGroupobj.getString("keywords"));
			 	    	insertpstmt.setInt(4,hitGroupobj.getInt("reward"));
			 	    	insertpstmt.setString(5,hitGroupobj.getString("description"));
			 	    	insertpstmt.setString(6,hitGroupobj.getString("requesterId"));					        
			 	    	insertpstmt.setString(7,hitGroupobj.getString("requesterName"));
			 	    	insertpstmt.setInt(8,hitGroupobj.getInt("timeAllotted"));
					        	
			 	    	int result=insertpstmt.executeUpdate();
			 	    	if(result==-1){
			 	    		System.out.println("insert error!");
			 	    		continue;
			 	    	}
			 	   	}	        
		 	    }catch(JSONException e){
		 	        	e.printStackTrace();
		 	        	System.out.println(nameApiUrl);
		 	        }	
					System.out.println((count++)+":"+rName);
				}
			
			writeFileByLines(preFileName,writeSet);	//update keyword set
			
	        insertpstmt.close();
	        conn.close();
	        System.out.println("updated");
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

}
