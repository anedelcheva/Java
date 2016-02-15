package multithreading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import logger.Logger;

public class MultiThreadedServer implements Runnable {
	
	private static final int SERVER_PORT = 10000;
	private static final Path PATH = Paths.get("/home/aneta/Desktop", "current.log");
	private static ServerSocket serverSocket;
	private Socket socket;
	private String username;
	private Logger logger;
	private static boolean serverIsActive = true;
	
	public MultiThreadedServer(Socket socket, Logger logger) 
			throws IOException {
		this.socket = socket;
		this.setLogger(logger);
	}
	
	public static void setServerIsActive(boolean serverIsActive) {
		MultiThreadedServer.serverIsActive = serverIsActive;
	}
	
	public static boolean isServerActive() {
		return serverIsActive;
	}

	public static void setServerActive(boolean serverIsActive) {
		MultiThreadedServer.serverIsActive = serverIsActive;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static void setServerSocket(ServerSocket serverSocket) {
		MultiThreadedServer.serverSocket = serverSocket;
	}
	
	public static boolean closeConnectionToServer() {
		return serverIsActive = false;
	}
	
	public static void startServerWith(Socket socket) throws IOException {
		Logger logger = new Logger(PATH);
		serverSocket = new ServerSocket(SERVER_PORT);
		while(serverIsActive) {
			socket = serverSocket.accept();
			acceptANewConnection(socket, logger);
			//closeConnectionToServer();
		}
	}
	
	@SuppressWarnings("resource")
	public static void receiveConnectionFromClient(ServerSocket serverSocket, Socket socket) 
			throws IOException {
		serverSocket = new ServerSocket(SERVER_PORT);
		socket = serverSocket.accept();
	}
	
	//for a testing purpose
	public static void acceptANewConnection(Socket socket, Logger logger) 
			throws IOException {
		new Thread(new MultiThreadedServer(socket, logger)).start();
	}
	
	public static void startServer() throws IOException {
		Logger logger = new Logger(PATH);
		serverSocket = new ServerSocket(SERVER_PORT);
		while(serverIsActive) {
			Socket socket = serverSocket.accept();
			acceptANewConnection(socket, logger);
			//closeConnectionToServer();
		}
	}
	
	private static void sendToClient(PrintWriter out, String message) {
		out.println(message);
		out.flush();
	}
	
	private static String answerFromClient(BufferedReader reader) {
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Listening...");
		MultiThreadedServer.startServer();
	}

	@Override
	public void run() {
		try(PrintWriter out = new PrintWriter(getSocket().getOutputStream());
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(getSocket().getInputStream()))) {
			setUsername(answerFromClient(reader));
			System.out.println("Client with username " + username + " connected");
			sendToClient(out, "Send your message: ");
			String message = answerFromClient(reader);
			System.out.println("Message from client " + username + " is: \"" + message + "\"");
			getLogger().log(username, message);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
