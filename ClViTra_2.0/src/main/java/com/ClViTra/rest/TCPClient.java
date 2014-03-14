package com.ClViTra.rest;

//TCPClient.java
//A client program implementing TCP socket
import java.net.*;
import java.io.*;

public class TCPClient {
	public static void RequestTranscoding (String ID)
	{// arguments supply message and hostname of destination
		Socket s = null;
		try{
			int serverPort = 6880;
			String ip = "localhost";
            String data = ID;
			  

            s = new Socket(ip, serverPort);
            DataInputStream input = new DataInputStream( s.getInputStream());
            DataOutputStream output = new DataOutputStream( s.getOutputStream());

			//Step 1 send length
			System.out.println("Length"+ data.length());
			output.writeInt(data.length());
			//Step 2 send length
			System.out.println("Writing.......");
			output.writeBytes(data); // UTF is a string encoding

			//Step 1 read length
			//int nb = input.readInt();
			//byte[] digit = new byte[nb];
			//Step 2 read byte
			//for(int i = 0; i < nb; i++)
				//digit[i] = input.readByte();

			//String st = new String(digit);
			//System.out.println("Received: "+ st);
		}
		catch (UnknownHostException e){
			System.out.println("Sock:"+e.getMessage());}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage()); }
		catch (IOException e){
			System.out.println("IO:"+e.getMessage());}
		finally {
			  	if(s!=null)
			  		try {
			  			s.close();
			  		}
				  	catch (IOException e) {/*close failed*/}
		}
	}

    /*private static String readEntireFile(String filename) throws IOException {
    	 FileReader in = new FileReader(filename);
    	 StringBuilder contents = new StringBuilder();
    	 char[] buffer = new char[4096];
    	 int read = 0;
    	 do {
    		 contents.append(buffer, 0, read);
    		 read = in.read(buffer);
    	 } while (read >= 0);
    	 return contents.toString();
     }*/
}



