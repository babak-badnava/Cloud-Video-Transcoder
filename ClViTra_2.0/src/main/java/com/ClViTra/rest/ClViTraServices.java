package com.ClViTra.rest;

//import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
//import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

//import org.apache.http.HttpResponse;








import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
//import com.sun.jersey.spi.dispatch.RequestDispatcher;
import com.xuggle.xuggler.IContainer;



//@Path("/users")
@Path("ClViTra")
public class ClViTraServices
{
	
   
   //UserRegistrationController urc;

   @POST
   //@Produces("text/plain")
   @Path("/videos")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response performUserLogin(@FormDataParam("user") String username,
        @FormDataParam("pwd") String password)
   {
	   //String output = "In LoginService.html"+ username + password;
	   String output = null;
	   int Return_code = confirmUserLoginDetails(username, password);
	   //String test = null;
	   
	   java.net.URI location = null;
	   
	   switch (Return_code) {
	   
	   case 0: {
		   
		   //try {
			   //location = new java.net.URI("http://localhost:8080/ClViTra_2.0/FileUpload.html");
			   //output = "SUCCESSFUL";
		   	//ObjectStore ob = new ObjectStore();
		   	//ob.ObjectStoreStart();
		   	
		    output = "<h1>ClViTra</h1>" + VideosDisplay();
			 //  throw new WebApplicationException(Response.temporaryRedirect(location).build());
		   //} catch (URISyntaxException e) {
			   // TODO Auto-generated catch block
			   //e.printStackTrace();
		   //}
		   //return Response.temporaryRedirect(location);
		   //break;
			   return Response.status(200).entity(output).build();
	   }
	   
	   case 1: {
		   
		   try {
			   location = new java.net.URI("http://137.226.58.21:8080/ClViTra_2.0/Login.html");
			   /*output = " <html> <body> <h1>Login to the ClViTra Service</h1> <form action=\"/login\" method=\"post\" "
			   		+ "enctype=\"multipart/form-data\"> <p> Username: <input type=\"text\" name=\"user\"><br> Password: "
			   		+ "<input type=\"password\" name=\"pwd\"> </p> <input type=\"submit\" value=\"Login\" /> </form> </body> </html>"; */
			   throw new WebApplicationException(Response.temporaryRedirect(location).build());
		   } catch (URISyntaxException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   
		   break;
	   }
	   
	   case 2: {
		   
		   
		   try {
			   location = new java.net.URI("http://137.226.58.21:8080/ClViTra_2.0/Login.html");
			   /*output = " <html> <body> <h1>Login to the ClViTra Service</h1> <form action=\"/login\" method=\"post\" "
			   		+ "enctype=\"multipart/form-data\"> <p> Username: <input type=\"text\" name=\"user\"><br> Password: "
			   		+ "<input type=\"password\" name=\"pwd\"> </p> <input type=\"submit\" value=\"Login\" /> </form> </body> </html>"; */
			   		
			   throw new WebApplicationException(Response.temporaryRedirect(location).build());
		   } catch (URISyntaxException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   
		   break;
	   }
	   
	   }
	   
	   return Response.temporaryRedirect(location).build();
       
	   //return Response.status(200).entity(output).build();
   }
   
   public int confirmUserLoginDetails(String username, String password) {
		
		
		new Java2MySql();// j2m;
		int Return_code = Java2MySql.LoginVerification(username, password);
		
		return Return_code;
	    /*User user = null;

	    try {
	        String queryArgument = "select u from User u where u.username = ?1 AND u.password = ?2";
	        Query query = em.createQuery(queryArgument);
	        query.setParameter(1, username);
	        query.setParameter(2, password);
	        user = (User) query.getSingleResult();

	    } catch (NoResultException e) {
	    }

	    if (user != null) {
	        return "index2";
	    } else {
	        return "No" + username;
	    }
	    */

   }
   
   public String VideosDisplay() {
	   String Name, Thumbnail, output = null;
	   output = "<form action=\"upload\" method=\"post\" enctype=\"multipart/form-data\"> "
	   		+ "<p> Select a file : <input type=\"file\" name=\"file\" size=\"45\" /> </p> <input type=\"submit\" value=\"Upload It\" /> </form> ";
	   List<String> myList = Java2MySql.TranscodedVideos();
	   while (!myList.isEmpty()) {
		   Name = myList.remove(0);
		   Thumbnail = myList.remove(0);
		   //output += "<a href=\""+Name+"\"> <img src=\"http://upload.wikimedia.org/wikipedia/commons/3/39/Bachelor%27s_button,_Basket_flower,_Boutonniere_flower,_Cornflower_-_3.jpg\" height=\"42\" width=\"75\"></a><br>";
		   
		   output += "<a href=\""+Name+"\"> <img src=\""+Thumbnail+"\" height=\"42\" width=\"75\"></a><br>";
	   }
	   //return Response.status(200).entity(output).build();
	   return output;

   }
   
   @POST
   @Path("/upload")
   @Consumes(MediaType.MULTIPART_FORM_DATA)
   public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
	   
	   	String uploadPath, savePath, thumbnailPath;
		String INPUT_FILE = "tempFileLocation";
		
		uploadPath = GetProperty.getParam("location", INPUT_FILE);
		savePath = GetProperty.getParam("savePath", INPUT_FILE);
		thumbnailPath = GetProperty.getParam("thumbnailPath", INPUT_FILE);

		String output = null;
		String ID = null;
		//java.net.URI location = null;
		//String uploadPath1 = "/usr/clvitra/";
		String uploadedFileLocation = uploadPath + fileDetail.getFileName();

		if (fileDetail.getFileName().endsWith(".mp4")) {
			// saving the file
			writeToFile(uploadedInputStream, uploadedFileLocation);
			// Generating Thumbnail
			String ThumbnailFilename = Thumbnail.Generate_Thumbnail(uploadPath, fileDetail.getFileName());
			File file = new File (ThumbnailFilename);
			ThumbnailFilename=file.getName();
			//Get the duration of the video
			long Duration = getDuration(uploadedFileLocation);
			System.out.println(savePath+"--"+ThumbnailFilename);
			// Entering in the Database
			ID = Java2MySql.VideoUpdate(savePath+fileDetail.getFileName(), "MP4", thumbnailPath+ThumbnailFilename, Duration);
			output = "<h1>ClViTra</h1> <p style=\"color:green\"> Upload Successful!</p>";
			output += VideosDisplay();
			//output = "File uploaded to : " + uploadedFileLocation;
			
		}
		
		else if (fileDetail.getFileName().endsWith(".avi")) {
			// saving the file
			writeToFile(uploadedInputStream, uploadedFileLocation);
			// Generating Thumbnail
			String ThumbnailFilename = Thumbnail.Generate_Thumbnail(uploadPath, fileDetail.getFileName());
			File file = new File (ThumbnailFilename);
			ThumbnailFilename=file.getName();
			//Get the duration of the video
			long Duration = getDuration(uploadedFileLocation);
			// Entering in the Database
			ID = Java2MySql.VideoUpdate(savePath+fileDetail.getFileName(), "AVI", thumbnailPath+ThumbnailFilename, Duration);
			//output = "File uploaded to : " + uploadedFileLocation;
			output = "<h1>ClViTra</h1> <p style=\"color:green\"> Upload Successful!</p>";
			output += VideosDisplay();
			
		}
		
		else if (fileDetail.getFileName().endsWith(".3gp")) {
			// saving the file
			writeToFile(uploadedInputStream, uploadedFileLocation);
			// Generating Thumbnail
			String ThumbnailFilename = Thumbnail.Generate_Thumbnail(uploadPath, fileDetail.getFileName());
			File file = new File (ThumbnailFilename);
			ThumbnailFilename=file.getName();
			//Get the duration of the video
			long Duration = getDuration(uploadedFileLocation);
			// Entering in the Database
			ID = Java2MySql.VideoUpdate(savePath+fileDetail.getFileName(), "3GP", thumbnailPath+ThumbnailFilename, Duration);
			//output = "File uploaded to : " + uploadedFileLocation;
			output = "<h1>ClViTra</h1> <p style=\"color:green\"> Upload Successful!</p>";
			output += VideosDisplay();
			
		}

		else if (fileDetail.getFileName().endsWith(".wmv")) {
			// saving the file
			writeToFile(uploadedInputStream, uploadedFileLocation);
			// Generating Thumbnail
			String ThumbnailFilename = Thumbnail.Generate_Thumbnail(uploadPath, fileDetail.getFileName());
			File file = new File (ThumbnailFilename);
			ThumbnailFilename=file.getName();
			//Get the duration of the video
			long Duration = getDuration(uploadedFileLocation);
			// Entering in the Database
			ID = Java2MySql.VideoUpdate(savePath+fileDetail.getFileName(), "WMV", thumbnailPath+ThumbnailFilename, Duration);
			//output = "File uploaded to : " + uploadedFileLocation;
			output = "<h1>ClViTra</h1> <p style=\"color:green\"> Upload Successful!</p>";
			output += VideosDisplay();
		}
		
		else {
			
			output = "<h1>ClViTra</h1> <p style=\"color:red\">Format not supported! Please try again. "
					+ "<br> Supported formats: MP4, AVI, WMV, 3GP. </p>";
					//+ "<br> Click <a href = \"http://localhost:8080/RESTfulExample/FileUpload.html\">back</a>to try again!";
					//+ "<form action=\"upload\" method=\"post\" enctype=\"multipart/form-data\"> <p> Select a file : "
					//+ "<input type=\"file\" name=\"file\" size=\"45\" /> </p> <input type=\"submit\" value=\"Upload It\" /> </form> ";
			output += VideosDisplay();
		}

		System.out.println("check");
		//Transcode_deprecated.transcode();
		//TCPClient.RequestTranscoding(ID);
		try {
			//System.out.println(ID);
			Send_C.send(ID);
			Recv_C.recv();
			System.out.println("passed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).entity(output).build();

	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private long getDuration(String movie) {
		
		IContainer container = IContainer.make();
	    //IURLProtocolHandler movie = null;
		if (container.open(movie, IContainer.Type.READ, null) < 0) {
	        throw new RuntimeException("Cannot open '" + movie + "'");
	    }
		
	    //logger.info("# Duration (ms): " + ((container.getDuration() == Global.NO_PTS) ? "unknown" : "" + container.getDuration() / 100));
	    //logger.info("# File size (bytes): " + container.getFileSize());
	    //logger.info("# Bit rate: " + container.getBitRate());
		return container.getDuration() / 1000000;
		
	}
   
   
   
}

 
