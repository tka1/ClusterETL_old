package etl;



import java.io.*;

import java.util.TimeZone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Properties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;




import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterEtl
{
 
	class CustomActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            ;
        }
     }
	
	 
	
    public static void main(String[] args) throws IOException, ParseException 
    {
    	
    	//get address & port from input dialog
        //String clusteraddress = JOptionPane.showInputDialog(null, "Enter  cluster address " );
        //String port = JOptionPane.showInputDialog(null, "Enter cluster port " );
        //int iport = Integer.parseInt(port);
    	String clusteraddress = null;
    	String call = null;
    	int iport = 0;
    	File configFile = new File("config.properties");

    	try {
    		FileReader reader = new FileReader(configFile);
    		Properties props = new Properties();
    		props.load(reader);

    		 clusteraddress = props.getProperty("address");
    		String port = props.getProperty("port");
    		call = props.getProperty("call");
    		  iport = Integer.parseInt(port);

    		System.out.print("Host name is: " + clusteraddress);
    		System.out.print("  port is: " + port);
    		reader.close();
    	} catch (FileNotFoundException ex) {
    		// file does not exist
    		System.out.print("conf file not found");
    	}
    	
    	
    	
 	    
    //Frame initialization
    	JButton bChange ;
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(clusteraddress);
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
     // construct a Button
        bChange = new JButton("Click Me!"); 
      

        panel.add( bChange );  

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
                           File file =new File("cluster_" + host + ".txt");
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
    	try
		{	
			s.setSoTimeout (30000);
		}
		catch (SocketException se)
		{
			System.err.println ("Unable to set socket option SO_TIMEOUT");
		}
        
        //Send message to server
       
                           String message = call;
                           s_out.println( message );
                                                                                  
                           System.out.println("Message send");
                                                      
                           //Get response from server
                           String response;
                           while (true){
                           try
                           {
                           while ((response = s_in.readLine()) != null) 
                           {				{
                           
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
                                                      String dxcontinent = country_2.substring((country_2.length()-2));
                                                      String finaldxcontinent = dxcontinent.replace("ll","");
                                                      System.out.println(dxcall + "  " +country_2);
                                                      
                                                      String de_continent = country.Search(decall);
                                                      String decontinent = de_continent.substring((de_continent.length()-2));
                                                      String finaldecontinent = decontinent.replace("ll","");
                                                      System.out.println("de continent  " + finaldecontinent);
                                                      System.out.println("dx continent  " + finaldxcontinent);
                                                      double freq = Double.parseDouble(response.substring(17, 25));
                                                      S_N = response.substring(40, 43).trim();
                                                      //S_N = response.substring(46, 49).trim();
                                                   
                                                      time = response.substring(70, response.length()).trim();
                                                      String mode = response.substring((response.length()-16),(response.length()-7)).trim();
                                                      String info = response.substring(38,(response.length()-7)).trim();
                                                      
                                                      //System.out.println(mode);
                                                      
                                                      StringBuffer newtime = new StringBuffer(time);
                                                      newtime.insert(2,  ":");
                                                      
                                                      String pattern = "yyyy-MM-dd";
                                                      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                                      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 
                                                      String date = simpleDateFormat.format(new Date());
                                                      String datetime = date + " " + newtime;
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
                                                      
                                            //Mongodb routines       
                                                    /*  try {
                                          	       // connect to mongo server
                                                    	  Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
                                                    	  mongoLogger.setLevel(Level.SEVERE);
                                                    	  
                                          	            MongoClient mongo = new MongoClient("localhost", 27017);
                                                    	 // MongoClient mongo = new MongoClient("localhost", 3001);

                                          	            // create a blank database
                                          	            DB db = mongo.getDB("cluster");
                                          	            
                                          	            DBCollection coll = db.getCollection("cluster");
                                          	            //System.out.println("Collection created successfully");
                                          	            
                                          	            BasicDBObject doc = new BasicDBObject("title", "cluster").
                                          	                    append("decall", decall).
                                          	                    append("dxcall", dxcall).
                                          	                    append("freq", freq).
                                          	                    append("band", band).
                                          	            		append("S/N", S_N).
                                          	            		append("datetime", datetime).
                                          	            		append("mode", mode).
                                          	            		append("country", country_2);
                                          	            
                                          	                 coll.insert(doc); 
                                          	                 //System.out.println("Document inserted successfully");
                                          	                 
                                          	        // statistics         
                                          	            BasicDBObject searchQuery = new BasicDBObject();
                                          	         	searchQuery.put("dxcall", dxcall);
                                          	         	DBCursor cursor = coll.find(searchQuery);
                                          	         	System.out.println(cursor.count());
                                          	         	System.out.println( coll.count(new BasicDBObject("band", new BasicDBObject("$gt", 130000)))); 
                                          	         	                                 	        
                                          	         	  mongo.close(); 

                                          	          
                                          	        } catch (Exception e) {
                                          	            System.out.println("Error in creating database.");
                                          	            e.printStackTrace();
                                          //Mongodb routine end
                                          	            } */
                                          // PostregSQL routines
                                                      Connection con = null;
                                                      Statement st = null;
                                                      ResultSet rs = null;
                                                      PreparedStatement pst = null;
                                                      
                                                      //date time format change
                                                      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                                      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki")); 
                                                      java.util.Date newdate = sdf.parse(datetime);
                                                      java.sql.Timestamp sqlDate = new Timestamp(newdate.getTime());
                                                      System.out.println(newdate);
                                                      System.out.println(datetime);
                                                      System.out.println(sqlDate);
                                                    //date time format change
                                                  
                                                      String url = "jdbc:postgresql://localhost/postgres";
                                                      String user = "postgres";
                                                      String password = "Powerday1!";
                                                                                                  
                                                     try {
                                                          con = DriverManager.getConnection(url, user, password);
                                                         
                                                          String stm = "INSERT INTO cluster.clustertable(title, decall, dxcall, freq, band, datetime,sig_noise, country, mode, de_continent, dx_continent, info) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                                          pst = con.prepareStatement(stm);
                                                          pst.setString(1, clusteraddress + ":" + iport);
                                                          pst.setString(2, decall);
                                                          pst.setString(3, dxcall); 
                                                          pst.setDouble(4, freq);
                                                          pst.setString(5, band);
                                                          pst.setTimestamp(6, sqlDate);
                                                          pst.setString(7, S_N);
                                                          pst.setString(8, country_2);
                                                          pst.setString(9, mode);
                                                          pst.setString(10, finaldecontinent);
                                                          pst.setString(11, finaldxcontinent);
                                                          pst.setString(12, info);
                                                          pst.executeUpdate();


                                                      } catch (SQLException ex) {
                                                          Logger lgr = Logger.getLogger(ClusterEtl.class.getName());
                                                          lgr.log(Level.SEVERE, ex.getMessage(), ex);
                                                          clusterTextArea.append(ex.getMessage() + "\n");

                                                      } finally {
                                                          try {
                                                              if (rs != null) {
                                                                  rs.close();
                                                              }
                                                              if (st != null) {
                                                                  st.close();
                                                              }
                                                              if (con != null) {
                                                                  con.close();
                                                              }

                                                          } catch (SQLException ex) {
                                                              Logger lgr = Logger.getLogger(ClusterEtl.class.getName());
                                                              lgr.log(Level.WARNING, ex.getMessage(), ex);
                                                          }
                                                      }
                                            //PostregSQL routines end
                           }
                           }
                           
                           }
                           }
                           catch (InterruptedIOException iioe)
                           {
                        	   clusterTextArea.append("Timeout occurred" + "\n");
                        	   System.out.println ("Timeout occurred - killing connection");
               				//s.close();

                           }
                          
                           //close the i/o streams
                           //s_out.close();
                           //in.close();
                                                                                  
                           //close the socket
                           //s.close();
                           
                          // bufferWritter.close();
                          // clusterTextArea.append("connections killed" + "\n");
                           System.out.println ("connections killed");
       
        
    
                           }
                           }
    
    
    }
    
   


