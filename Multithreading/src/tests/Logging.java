package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import logger.Logger;
import multithreading.Client;
import multithreading.MultiThreadedServer;

import org.junit.Test;

public class Logging {
	
	public static final int SERVER_PORT = 10000;
	//private static final Path path = Paths.get("/home/aneta/Desktop/", "test.log");
	ServerSocket serverSocket;
	Socket socket;
	Client client1 = new Client();
	
	@Test
	public void testConnectionToServer() {
		MultiThreadedServer.setServerActive(false);
		assertEquals(false, MultiThreadedServer.isServerActive());
	}
	
	@Test
	public void testIfConnectionToServerIsClosed() {
		MultiThreadedServer.setServerActive(true);
		MultiThreadedServer.closeConnectionToServer();
		assertEquals(false, MultiThreadedServer.isServerActive());
	}
	
	@Test
	public void testIfClientIsConnectedSuccessfully() {
		
	}
	

//	@Test
//	public void test() throws IOException {
//		Path path = Paths.get("/home/aneta/Desktop", "test.log");
//		Logger logger = new Logger(path);
//		Client client1 = new Client();
//		Client client2 = new Client();
//		MultiThreadedServer.startServerWith(client1.getSocket());
//		MultiThreadedServer.startServerWith(client2.getSocket());
//	}
}
