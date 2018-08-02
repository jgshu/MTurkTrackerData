import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataStatistic {
	public static void main(String[] args){
	File file = new File("plainIndex.txt");
    BufferedReader reader = null;
    int keywordCount=0;
    int lineCount=0;
   // int[] KeyNum=new int[10];
    try {   
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;   
        while ((tempString = reader.readLine()) != null) {
        	String[] keywordSet=tempString.replace("\r\n", "").split(":")[1].split(","); 
        	
        	//System.out.println(keywordSet.length);
        	
        	//KeyNum[keywordSet.length-1]+=1;
        	keywordCount+=keywordSet.length;
        	
        	lineCount++;
        	if(lineCount==1000||lineCount==10000||lineCount==100000||lineCount==1000000){
        		String res=lineCount+":"+keywordCount;
        		System.out.println(res);
        		//System.out.print(keywordCount);
        		
        	}
                  	        
        }
        
        reader.close();
        //for (int i=0;i<10;i++)
        	//System.out.println((double)KeyNum[i]/1000000);
        
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
	}

}
