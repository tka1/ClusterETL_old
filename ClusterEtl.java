package etl;


import java.io.*;
import java.util.Scanner;
import java.util.TimeZone;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.DateFormat;
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
import java.text.SimpleDateFormat;


import com.mongodb.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterEtl
{

                            
                             
                            
    public static void main(String[] args) throws IOException, ParseException 
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
                                                      //S_N = response.substring(46, 49).trim();
                                                      time = response.substring(70, 74);
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
                                                         
                                                          String stm = "INSERT INTO cluster.clustertable(decall, dxcall, freq, band, datetime,sig_noise, country) VALUES(?, ?, ?, ?, ?, ?, ?)";
                                                          pst = con.prepareStatement(stm);
                                                          pst.setString(1, decall);
                                                          pst.setString(2, dxcall); 
                                                          pst.setDouble(3, freq);
                                                          pst.setString(4, band);
                                                          pst.setTimestamp(5, sqlDate);
                                                          pst.setString(6, S_N);
                                                          pst.setString(7, country_2);
                                                          pst.executeUpdate();


                                                      } catch (SQLException ex) {
                                                          Logger lgr = Logger.getLogger(ClusterEtl.class.getName());
                                                          lgr.log(Level.SEVERE, ex.getMessage(), ex);

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
                           //close the i/o streams
                           s_out.close();
                           //in.close();
                                                                                  
                           //close the socket
                           s.close();
                           
                           bufferWritter.close();
    }
   
}

