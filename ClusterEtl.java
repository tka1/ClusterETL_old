package telnet_client;

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.*;

public class ClusterEtl
{
 
	
	 
	
    public static void main(String[] args) throws IOException 
    {
    	
    	//get address & port from input dialog
        String clusteraddress = JOptionPane.showInputDialog(null, "Enter  cluster address " );
        String port = JOptionPane.showInputDialog(null, "Enter cluster port " );
        int iport = Integer.parseInt(port);
 	    
    //Frame initialization
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("My First Swing Example");
        // Setting the width and height of frame
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();    
        // adding panel to frame
        frame.add(panel);
        panel.setLayout(null);
        JTextArea  clusterTextArea = new JTextArea("");
        clusterTextArea.setFont(clusterTextArea.getFont().deriveFont(16f)); 
        JScrollPane scrollPane = new JScrollPane(clusterTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        clusterTextArea.setBounds(100,65,100,400);
       
        scrollPane.setPreferredSize(new Dimension(700, 700));
        panel.setLayout(new FlowLayout());
        //panel.add(clusterTextArea);
        panel.add(scrollPane);
        frame.setVisible(true);
   //Frame initialization
        
   // parameter initializations            
        CountrySearch2 country = new CountrySearch2();       
    	String decall= null;
        String decall_trimmed= null;
        String dxcall= null;
        //Double freq = null;
        String S_N = null;
        String time = null;
        String band = null;
        boolean newfile = false;
        
        String host = clusteraddress;
        
  //log file initialization
                           PrintWriter s_out = null;
                           BufferedReader s_in = null;
                           File file =new File("cluster_" + clusteraddress + ".txt");
                           //if file does'nt exists, then create it
                           if(!file.exists()){
                        	    newfile = true;
                        	       file.createNewFile();
                           }
                           FileWriter fileWritter = new FileWriter(file.getName(),true);
                           BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
   // write header if file not exits
        if(newfile) {
        	 bufferWritter.write("decall;dxcall;freq;band;S/N;datetime;country;continent");
             bufferWritter.newLine();
             bufferWritter.flush();
        }
        
        


    //telnet connection  
        Socket s = new Socket();
        try 
        {
            s.connect(new InetSocketAddress(host , iport));
            System.out.println("Connected ");
                                                                                  
          //writer for socket
            s_out = new PrintWriter( s.getOutputStream(), true);
            //reader for socket
            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }
        
        //Host not found
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know about host : " + host);
            System.exit(1);
        }
        
        //Send message to server
                           String message = "OH2BBT";
                           s_out.println( message );
                                                                                  
                           System.out.println("Message send");
                                                      
                           //Get response from server
                           String response;
                           while ((response = s_in.readLine()) != null) 
                           {
                                                      System.out.println( response );
                                                     // write line
                                                      clusterTextArea.append(response + "\n");
                                                      //focus to last line
                                                      String ss = clusterTextArea.getText();
                                                      int pos = ss.length();
                                                      clusterTextArea.setCaretPosition(pos);

                                                                                                                                                        
                                                      if (response.startsWith("DX")) {
                                                                                                                                                                                                                                   
                                                      //String country2 = ScannerReadFile(dxcall);
                                                       decall = (response.substring(5, 16));
                                                                                  decall = decall.trim();
                                                                                  decall_trimmed = decall.substring(0, decall.length()-1);
                                                      //System.out.println(decall_trimmed);
                                                      dxcall = response.substring(26, 38);
                                                      dxcall = dxcall.trim();
                                                      //String shortdxcall = dxcall.substring(0,4);
                                                      String country_2 = country.Search(dxcall);
                                                      System.out.println(dxcall + "  " +country_2);
                                                      double freq = Double.parseDouble(response.substring(17, 25));
                                                      S_N = response.substring(40, 43).trim();
                                                      time = response.substring(70, 74);
                                                      StringBuffer newtime = new StringBuffer(time);
                                                      newtime.insert(2,  ":");
                                                      
                                                      String pattern = "yyyy-MM-dd";
                                                      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                                      String date = simpleDateFormat.format(new Date());
                                                      if (freq >= 1800 && freq <= 1900 ) {
                                                    	  band = "160M";
                                                      }
                                                      
                                                      if (freq >= 3500 && freq <= 3700 ) {
                                                    	  band = "80M";
                                                      }
                                                      
                                                      if (freq >= 7000 && freq <= 7200 ) {
                                                    	  band = "40M";
                                                      }
                                                      
                                                      if (freq >= 10100 && freq <= 10150 ) {
                                                    	  band = "30M";
                                                      }
                                                      
                                                      if (freq >= 14000 && freq <= 14500 ) {
                                                    	  band = "20M";
                                                      }
                                                      
                                                      if (freq >= 18000 && freq <= 19000 ) {
                                                    	  band = "17M";
                                                      }
                                                      
                                                      if (freq >= 21000 && freq <= 21500 ) {
                                                    	  band = "15M";
                                                      }
                                                      
                                                      if (freq >= 28000 && freq <= 28500 ) {
                                                    	  band = "10M";
                                                      }
                                                      
                                                      if (freq >= 50000 && freq <= 51500 ) {
                                                    	  band = "6M";
                                                      }
                                                      
                                                      
                                                // write line to log file      
                                                      bufferWritter.write(decall + ";" + dxcall + ";" + freq + ";" + band + ";" + S_N + ";" + date + " " + newtime + ";" + country_2);
                                                      bufferWritter.newLine();
                                                      bufferWritter.flush();
                                                      //System.out.println(decall + ";" + dxcall + ";" + freq + ";" + S_N + ";" + date + " " + newtime);
                           }
                           }
                           //close the i/o streams
                           s_out.close();
                           //in.close();
                                                                                  
                           //close the socket
                           s.close();
                           
                           bufferWritter.close();
    }
   
}
