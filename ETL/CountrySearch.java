package etl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class CountrySearch {
	public  String Search(String find) {
		
        String cvsSplitBy = ":";
        String cvsSplitBy2 = ",";
        String continent = null;
        String country = "null";
        String[] Prefix = null;
        int row = 0;
        String[] countryValues = new String[10000];
        
       // reading rows from file into array
        try (BufferedReader br = new BufferedReader(new FileReader("wl_cty.txt")))
        {
        	String line = br.readLine();
       while (line != null)  {
    	   	            	 countryValues[row] = line;
            	           	 line = br.readLine();
            	           	 row++;                        	
            				}
              // looping all rows and match callsign
    
              for (int i = 0; i < row ; i++){
            	  
            	 //find country row
            	  if (":".equals(countryValues[i].substring(countryValues[i].length() - 1))){
            		              		  
            		   int prefrow = 1;
            		//find all prefix rows           		 
            		while (!":".equals(countryValues[i+prefrow].substring(countryValues[i+prefrow].length() - 1))) {
            	           			String[] countryLine = countryValues[i].split(cvsSplitBy);
            	           			Prefix = countryValues[i + prefrow].split(cvsSplitBy2);
            	         	        
            	           			for (int ii = 0; ii < Prefix.length ; ii++){
            	           				String pref  = Prefix[ii].replaceAll(";|=","");
            	           				if (pref.indexOf("(")!=-1){
            	           					pref = pref.substring(0, pref.indexOf("("));
            	           						}	
            		 	
            	           				if (find.startsWith(pref.trim())) {
            	           					country = countryLine[0];
            	           					continent = countryLine[3];
            	           					break;
            	           				}
            	}
                  	
            	prefrow++ ;
            	      }
            	  }
            	 //System.out.println(i);
            	  if (!country.equals("null")) {
		 				break;
		 			}
              }
           
        } catch (IOException  e) 
        {
           System.out.println("File I/O error");
        }
        catch (NullPointerException  e) 
        {
          //System.out.println("NullPointerException");
           //e.printStackTrace();
           country =null;
           continent = null;
        }
 return country + ";" + continent ;
    }




}



