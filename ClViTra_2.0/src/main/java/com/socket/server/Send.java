package com.socket.server;
import com.ClViTra.rest.GetProperty;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {

	private final static String QUEUE_NAME = "Send";
	private static String server;
	private final static String INPUT_FILE = "RabbitMQ";

	public static void send(String status) throws Exception {
      	      
		server = GetProperty.getParam("server", INPUT_FILE);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(server);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    //	String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, status.getBytes());
    //	System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();
	}
}	