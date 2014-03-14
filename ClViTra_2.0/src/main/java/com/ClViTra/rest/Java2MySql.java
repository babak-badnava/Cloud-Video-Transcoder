package com.ClViTra.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.sun.jersey.core.header.FormDataContentDisposition;
 
 
public class Java2MySql
{
	private final String INPUT_FILE = "dbconnection";
	private static String url;// = "jdbc:mysql://localhost:3306/";
	private static String hostName;
	private static String dbName;// = "clvitra";
	private static String driver;// = "com.mysql.jdbc.Driver";
	private static String userName;// = "root";
	private static String password;// = "root";
	private static String databaseServer;
	private static String useUniCode;
	private static String charEncoding;
	private static String charSet;
	private static String collation;
	
	
	public Java2MySql() {

			
			driver = GetProperty.getParam("driverName", INPUT_FILE);
			databaseServer = GetProperty.getParam("databaseServer", INPUT_FILE);
			hostName = GetProperty.getParam("hostName", INPUT_FILE);
			dbName = GetProperty.getParam("database", INPUT_FILE);
			userName = GetProperty.getParam("username", INPUT_FILE);
			password = GetProperty.getParam("password", INPUT_FILE);
			useUniCode = GetProperty.getParam("useUniCode", INPUT_FILE);
			charEncoding = GetProperty.getParam("charEncoding", INPUT_FILE);
			charSet = GetProperty.getParam("charSet", INPUT_FILE);
			collation = GetProperty.getParam("collation", INPUT_FILE);
			
			url = "jdbc:" + databaseServer + "://" + hostName + "/";
			
	}
	

	public static int LoginVerification(String u_username, String u_password) {
          
          boolean authentication = false;
          boolean user_available = false;
          int Return_code;
          
          try {
        	  Class.forName(driver).newInstance();
        	  Connection conn = DriverManager.getConnection(url+dbName,userName,password);
           
        	  Statement st = conn.createStatement();
        	  ResultSet res = st.executeQuery("SELECT * FROM  user");
        	  while (res.next() && user_available==false) {
        		  // Checking for user availability in the database.
        		  if(res.getString("username").compareTo(u_username)==0) {
        			  // Marking user as available.
        			  user_available = true;
        			  // Converting the entered password into MD5 hash to become eligible to be compared with stored password. 
        			  java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        			  byte[] array = md.digest(u_password.getBytes());
        			  StringBuffer converted_pwd = new StringBuffer();
                  
        			  for (int i = 0; i < array.length; ++i) {
        				  converted_pwd.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        			  }
        			  
        			  // Comparing the entered password with the stored password.
        			  if(res.getString("password").compareTo(converted_pwd.toString())==0) {
        				  //Marking password to be correct
        				  authentication = true;		  
        			  }
        		  }
        	  }
        	  //int val = st.executeUpdate("INSERT into event VALUES("+1+","+"'Easy'"+")");
        	  //if(val==1)
              	//System.out.print("Successfully inserted value");
          
        	  conn.close();
          } catch (Exception e) {
        	  e.printStackTrace();
          }
         
          if (user_available && authentication) {
        	  Return_code = 0;
        	  //Return_String = "Login Successful!";
          }
          else if (user_available && !authentication) {
        	  Return_code = 1;
        	  //Return_String = "Wrong Password!";
          }
          else {
        	  Return_code = 2;
        	  //Return_String = "User not registered!";
          }
       
          return Return_code;
    }
	
	public static String VideoUpdate(String filename, String ext, String ThumbnailFilename, long Duration) {
        
		PreparedStatement pstmt = null;
        //int Return_code;
        int rowCount = 0;
        UUID ID = null;
        
        try {
        	Class.forName(driver).newInstance();
      	  	Connection conn = DriverManager.getConnection(url+dbName,userName,password);
      	  	ID = UUID.randomUUID();
      	  	//Statement st = conn.createStatement();
      	  	String insertQuery = "INSERT INTO video (ID, Name, Format, Status, Duration, Thumbnail) VALUES (?,?,?,?,?,?)";
      	  	
      	  	pstmt = conn.prepareStatement(insertQuery);
      	  	pstmt.setString(1, ID.toString());
      	  	pstmt.setString(2, filename);
      	  	pstmt.setString(3, ext);
      	  	if(ext == "MP4")
      	  		pstmt.setString(4, "TRANSCODED");
      	  	else
      	  		pstmt.setString(4, "INITIALIZED");
      	  	pstmt.setLong(5, Duration);
      	  	pstmt.setString(6, ThumbnailFilename);
      	  	System.out.println(ThumbnailFilename);
      	  	rowCount = pstmt.executeUpdate();
      	  	
      	  	//st.executeUpdate();
      	  	conn.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return ID.toString();
	}
	
	public static int VideoUpdate(String ID, String outputFile, String URI) {
        
		PreparedStatement pstmt = null;
        int rowCount = 0;
        
        try {
        	Class.forName(driver).newInstance();
      	  	Connection conn = DriverManager.getConnection(url+dbName,userName,password);
      	  	String insertQuery = "UPDATE  video SET  Status = ?, Name = ?, Format = ?, URI = ? WHERE  ID = ?";
      	  	
      	  	pstmt = conn.prepareStatement(insertQuery);
      	  	pstmt.setString(1, "TRANSCODED");
      	  	pstmt.setString(2, new File(outputFile).getName());
      	  	pstmt.setString(3, "MP4");
      	  	pstmt.setString(4, URI);
      	  	pstmt.setString(5, ID);
      	  	rowCount = pstmt.executeUpdate();
      	  	
      	  	conn.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return rowCount;
	}

	public static String getFirstInitializedVideo() {
	
		String ID = null;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String insertQuery = "SELECT * FROM  video WHERE Status=?";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
			pstmt.setString(1, "INITIALIZED");
			ResultSet res = pstmt.executeQuery();
			
			
  	  
			//if(res.getString("Status").compareTo("INITIALIZED")==0)
			if (res.next())	
				ID = res.getString("ID");
			

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;
	}
	
	public static void Processing(String ID) {
		
		try {
        	Class.forName(driver).newInstance();
      	  	Connection conn = DriverManager.getConnection(url+dbName,userName,password);
      	  	String insertQuery = "UPDATE  video SET  Status = ? WHERE  ID = ?";
      	  	PreparedStatement pstmt = null;
      	  	pstmt = conn.prepareStatement(insertQuery);
      	  	pstmt.setString(1, "PROCESSING");
      	  	pstmt.setString(2, ID);
      	  	pstmt.executeUpdate();
      	  	
      	  	conn.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static String getVideoName(String ID) {
		
		String Name = null;
		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			String insertQuery;

			insertQuery = "SELECT * FROM  video WHERE ID=?";
			PreparedStatement pstmt = conn.prepareStatement(insertQuery);
      	  	
      	  	pstmt.setString(1, ID);
      	  	ResultSet res = pstmt.executeQuery();
      	  	if (res.next())
      	  		Name = res.getString("Name");

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Name;
	}
	
	public static List TranscodedVideos(){
		List<String> myList = new ArrayList<String>();
        //myList.add("java");
		String Thumbnail, Name;
        try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url+dbName,userName,password);
			
			String Query = "SELECT * FROM video WHERE Status = ?";
			PreparedStatement pstmt = conn.prepareStatement(Query);
			pstmt.setString(1, "TRANSCODED");
			ResultSet res = pstmt.executeQuery();
			
			while (res.next()) {
				Name = res.getString("URI");
				Thumbnail = res.getString("Thumbnail");
				myList.add(Name);
				myList.add(Thumbnail);
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return myList;
	}
	
}