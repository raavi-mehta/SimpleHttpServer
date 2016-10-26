import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WebServer extends Thread {
	
	Scanner inputStream;
    PrintWriter outputStream;
    ServerSocket serverSocket;
	
	public WebServer(int port) {
			
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("I/O error when creating socket, please try again.");
		}
			
	}
	
	public void run() {
		
		while(true) {
			
			try {
				
				System.out.println("Waiting for connection request ...");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted request for connection.");
				
				inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
				
				System.out.println("Established connection.");
				new Thread(new WebServerWorker(clientSocket, inputStream, outputStream)).start();
				
			} catch (IOException e) {
				System.err.println("Error establishing connection, please try again.");
			}
			
			try {
				//TODO comment out
				System.out.println("Waiting for connection request ...");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted request for connection.");
				
				inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
				
				System.out.println("Established connection.");
				new Thread(new WebServerWorker(clientSocket, inputStream, outputStream)).start();
				
			} catch (IOException e) {
				System.err.println("Error establishing connection, please try again.");
			}
			
		}
		
	}
	
	public void shutdown() {
		
	}

}
