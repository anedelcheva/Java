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
	private String clientID;
	
	public Client() throws UnknownHostException, IOException {
		this.socket = new Socket(HOSTNAME, SERVER_PORT);
		this.setClientID(java.lang.management.ManagementFactory.getRuntimeMXBean().getName());
	}
	
	public Socket getSocket() {
		return socket;
	}
	
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
	
	public String getClientID() {
		return clientID;
	}

	public void setClientID(String username) {
		this.clientID = username;
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
	
	private void retryToConnect() {
		while(!isServerActive()) {
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				this.socket = new Socket(HOSTNAME, SERVER_PORT);
				@SuppressWarnings("unused")
				PrintWriter out = new PrintWriter(this.socket.getOutputStream());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void sendMessageToServer(PrintWriter out, String message) {
		if(isServerActive())
			sendToServer(out, message);
		else {
			retryToConnect();
			sendMessageToServer(out, message);
		}
	}
	
	public void start() throws IOException {
		System.out.println("Client with username " + this.getClientID() + " connected to server");
		try(PrintWriter out = new PrintWriter(this.getSocket().getOutputStream());
				Scanner scanner = new Scanner(System.in);
				BufferedReader reader = 
					new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()))) {
			sendToServer(out, this.clientID);
			System.out.print(answerFromServer(reader));
			String message = scanner.nextLine();
			sendToServer(out, message);
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client();
		client.start();
		}
	}
