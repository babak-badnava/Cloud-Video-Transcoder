package com.ClViTra.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.socket.server.Recv;
//import com.socket.server.Transcode;
import com.sun.jersey.multipart.FormDataParam;

@Path("")
public class Configuration {
	
	String status;
	
	@POST
	@Path("/config")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response performUserLogin(@FormDataParam("func") String func)
	{
		if (func.equals("Master"))
			status = "<html><body> Configuration set as MASTER! </body></html>";
		
		
		else if(func.equals("Slave")) {
			Recv.recv();
			status = "<html><body> Configuration set as SLAVE! </body></html>";
		}
		
		System.out.println(func);
		return Response.status(200).entity(status).build();
	}

}
