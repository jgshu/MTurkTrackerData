import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
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
	public static void main(String[] args){

		char[] chars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int length=14;
		char[] ID = new char[length];
		ID[0]='A';

		for(int i1=5;i1<chars.length;i1++){
			ID[1]=chars[i1];	
			for(int i2=0;i2<chars.length;i2++){
				ID[2]=chars[i2];
				for(int i3=0;i3<chars.length;i3++){
					ID[3]=chars[i3];
					for(int i4=0;i4<chars.length;i4++){
						ID[4]=chars[i4];
						for(int i5=0;i5<chars.length;i5++){
							ID[5]=chars[i5];
							for(int i6=0;i6<chars.length;i6++){
								ID[6]=chars[i6];
								for(int i7=0;i7<chars.length;i7++){
									ID[7]=chars[i7];
									for(int i8=0;i8<chars.length;i8++){
										ID[8]=chars[i8];
										for(int i9=0;i9<chars.length;i9++){
											ID[9]=chars[i9];
											for(int i10=0;i10<chars.length;i10++){
												ID[10]=chars[i10];
												for(int i11=0;i11<chars.length;i11++){
													ID[11]=chars[i11];
													for(int i12=0;i12<chars.length;i12++){
														ID[12]=chars[i12];
														for(int i13=0;i13<chars.length;i13++){
															ID[13]=chars[i13];
															String IDstr=new String(ID);
															//IDstr="A13512UCB235WN";
															String IDUrl="https://crowd-power.appspot.com/_ah/api/mturk/v1/requester?requesterId="+IDstr;       
															String IDContent=loadJson(IDUrl);
															System.out.println(IDstr);
															if(!IDContent.equals("")){	
																try{
																	FileWriter writer=new FileWriter("ValidRequesterID5.txt",true);
																	writer.write(IDContent);
																	writer.write("\r\n");
														        	writer.close();
														    	} catch (IOException e) {
														    		// TODO Auto-generated catch block
														    		e.printStackTrace();
														    	}      
														    	
														        
															}
																
														}
													}
												}
											}
										}
										
									}
									
								}
							}
						}			
					}					
				}			
			}
		}
		
		
				
					
						
							
							
						
					
	
	  

		
        
	}

	
}
