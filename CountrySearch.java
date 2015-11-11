package telnet_client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountrySearch {
	public  String Search(String find) {
		 
        // Location of file to read
        File file = new File("dxcc_list.txt");
        String cvsSplitBy = ",";
        String continent = null;
        String maa = null;
        int row = 0;
        //String find = "OH";
        //System.out.println(find);
        String[] countryValues = new String[1000];
        try {
 
            Scanner scanner = new Scanner(file);
                 
            while (scanner.hasNextLine()) {
            	 String line = scanner.nextLine();
            	 countryValues[row] = line;
            	 row++;
                        	
            }
            scanner.close();
            //System.out.println(row);
            
            for (int i = 1; i < row ; i++){
            	String[] countryLine = countryValues[i].split(cvsSplitBy);
            	//pref = countryLine[0];
            	//if (countryLine[0].equals(find)) {
            	//if (countryLine[0].startsWith(find)) {
            		if (find.startsWith(countryLine[0])) {
            		 maa = countryLine[1];
            		 continent = countryLine[2];
            		 break;
            		//return countryLine[1];	
            	}
            	
            }
            
            //System.out.println(countryValues[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
 return maa + ";" + continent ;
    }

}
