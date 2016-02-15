package multithreading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private static final int SERVER_PORT = 10000;
	private static final String HOSTNAME = "localhost";
	private Socket socket;
	private String username;
	
	public Client() {
		try {
			socket = new Socket(HOSTNAME, SERVER_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setUsername(java.lang.management.ManagementFactory.getRuntimeMXBean().getName());
	}
	
	public Socket getSocket() {
		return socket;
	}

	/*public void setSocket(Socket socket) {
		this.socket = socket;
	}*/
	
	public static boolean isServerActive() {
		try(Socket s = new Socket(HOSTNAME, SERVER_PORT)) {
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	private static void sendToServer(PrintWriter out, String message) {
		out.println(message);
		out.flush();
	}
	
	private static String answerFromServer(BufferedReader reader) {
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void main(String[] args) {
		
		Client client = new Client();
		System.out.println("Client with username " + client.getUsername() + " connected to server");
		
		// we send the client's username to the server
		try(PrintWriter out = new PrintWriter(client.getSocket().getOutputStream());
				Scanner scanner = new Scanner(System.in);
				BufferedReader reader = 
					new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()))) {
			sendToServer(out, client.getUsername());
			System.out.print(answerFromServer(reader));
			String message = scanner.nextLine();
			sendToServer(out, message);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
