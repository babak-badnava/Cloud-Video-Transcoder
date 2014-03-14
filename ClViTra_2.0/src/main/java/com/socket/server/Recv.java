package com.socket.server;
import java.io.File;
import java.io.IOException;

import com.ClViTra.rest.*;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Recv implements Runnable {

    private final static String QUEUE_NAME = "Receive";
    private static String server;
	private final static String INPUT_FILE = "RabbitMQ";
    
    public static void recv() {
    	
    	System.out.println("start");
		(new Thread(new Recv())).start();
		
	}

    public void run() {

    	server = GetProperty.getParam("server", INPUT_FILE);
    	ConnectionFactory factory = new ConnectionFactory();
    	factory.setHost(server);
    	Connection connection;
    	try {
    		connection = factory.newConnection();
    		Channel channel = connection.createChannel();
    		
    		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    		new Java2MySql();
    		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    		
    		QueueingConsumer consumer = new QueueingConsumer(channel);
    		channel.basicConsume(QUEUE_NAME, true, consumer);
    		
    		while (true) {
    			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
    			String ID = new String(delivery.getBody());
	      //	String status = 
    			System.out.println(ID);
    			System.out.println("test0");
    			Java2MySql.Processing(ID);
    			Transcode.transcode(ID);
	      //	String send_message = message + "%" + status;
	      //	Send.send(status);
	      //	System.out.println(" [x] Received '" + message + "'" + status + "  " + status);
    		}
    	} catch (IOException e) {
		// 	TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}	

