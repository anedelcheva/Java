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
	
//	public static boolean isServerActive() {
//		try(Socket s = new Socket(HOSTNAME, SERVER_PORT)) {
//			return true;
//		} catch (UnknownHostException e) {
//			//e.printStackTrace();
//		} catch (IOException e) {
//			//e.printStackTrace();
//		}
//		return false;
//	}
	
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
	
//	private void retryToConnect(PrintWriter out, BufferedReader reader) {
//		while(!isServerActive()) {
//			int i = 0;
//			try {
//				this.socket = new Socket(HOSTNAME, SERVER_PORT);
//				out = new PrintWriter(this.getSocket().getOutputStream());
//				reader = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
//				
//			} catch (UnknownHostException e) {
//				//e.printStackTrace();
//			} catch (IOException e) {
//				//e.printStackTrace();
//			}
//			System.out.println("Reconnect:" + i++);
//		}
//	}
	
//	private void sendMessageToServer(PrintWriter out, String message) {
//		if(isServerActive())
//			sendToServer(out, message);
//		else {
//			retryToConnect();
//			sendMessageToServer(out, message);
//		}
//	}
	
	public void start() throws IOException {
		System.out.println("Client with username " + this.getClientID() + " connected to server");
		try(PrintWriter out = new PrintWriter(this.getSocket().getOutputStream());
				Scanner scanner = new Scanner(System.in);
				BufferedReader reader = 
					new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()))) {
			sendToServer(out, this.clientID);
			
			String message = "";
			while (!message.equals("quit")) {
				String message2 = answerFromServer(reader);
				if (message2 != null) {
					System.out.print(message2);
				} else {
					System.out.print("Send your message: ");
				}
				
				message = scanner.nextLine();
				sendToServer(out, message);
			}
			
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client();
		client.start();
		}
	}
