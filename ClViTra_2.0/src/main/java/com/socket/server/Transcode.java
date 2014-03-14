package com.socket.server;

import java.io.File;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.ClViTra.rest.*;

public class Transcode implements Runnable {
	
	//private static String ID;
	private static String Return_value;
	
	public void run() {
	}
	
	public Transcode(String ID) {
		
		String path;
		String INPUT_FILE = "tempFileLocation";
		
		//String ID = null;
    	// Do your job here.
        //ID = Java2MySql.getFirstInitializedVideo();
        if (ID!=null) {
        	path = GetProperty.getParam("location", INPUT_FILE);
        	//Java2MySql.Processing(ID);
        	String VideoName = Java2MySql.getVideoName(ID);
        	System.out.println("VIDIO NAME:  "+VideoName);
        	File inputFile = new File(VideoName);
        	System.out.println("INPUT FILE ABS:  "+inputFile.getPath());
        	String VideoNameWithoutExt = VideoName.substring(0, VideoName.length()- 4);
        	System.out.println("VIDIO NAME W/O EXT:  "+VideoNameWithoutExt);
        	System.out.println("OUTPUT:  " +path + VideoNameWithoutExt.substring(30)+".mp4");
        	File outputFile = new File(path + VideoNameWithoutExt.substring(30)+".mp4");
        	System.out.println("OUTPUT FILE ABS:  "+outputFile.getAbsolutePath());
            if (transcoder(inputFile, outputFile)) {
            	
            	ObjectStore ob = new ObjectStore();
                //System.out.println("1");
    		   	String URI = ob.ObjectStoreStart(outputFile.getPath());
    		   	//Java2MySql.VideoUpdate(ID, outputFile.getPath(), URI);
    		   	Return_value= ID+"%"+outputFile.getPath()+"%"+URI+"%success";
            }
        }
        try {
			Send.send(Return_value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void transcode(String ID) {
		
		//ID = RID;
		(new Thread(new Transcode(ID))).start();
		//return Return_value;
	}
	
    public static boolean transcoder(File inputFile, File outputFile) {
        try {
        //Create an IMediaReader using the ToolFactory and the pat to the input file.
        IMediaReader reader = ToolFactory.makeReader(inputFile.getPath());
        
         
        //Attach a listener to the reader.  The listener is an IMediaWriter.
        reader.addListener(ToolFactory.makeWriter(outputFile.getAbsolutePath(), reader));

        //outputs it to the given file in the requested format.
        while(reader.readPacket() == null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
         
        return true;
    }
	
	
	
}