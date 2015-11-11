package telnet_client;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class CountrySearch2 {
	public  String Search(String find) {
		
        String cvsSplitBy = ",";
        String continent = null;
        String maa = null;
        int row = 0;
        String[] countryValues = new String[1000];
        
       // reading rows from file into array
        try (BufferedReader br = new BufferedReader(new FileReader("dxcc_list.txt")))
        {
        	String line = br.readLine();
       while (line != null)  {
            	           	 countryValues[row] = line;
            	           	 line = br.readLine();
            	           	 row++;                        	
            				}
              // looping all rows and match callsign
              for (int i = 1; i < row ; i++){
            	String[] countryLine = countryValues[i].split(cvsSplitBy);
            	if (find.startsWith(countryLine[0])) {
            		 maa = countryLine[1];
            		 continent = countryLine[2];
            		 break;
            			
            	}
               }
            
            //System.out.println(countryValues[0]);
        } catch (IOException  e) 
        {
           System.out.println("File I/O error");
        }
 return maa + ";" + continent ;
    }

}

