import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends Thread {
	
	private int port;
	private Scanner inputStream;
    private PrintWriter outputStream;
    private ServerSocket serverSocket;
    private List<WebServerWorker> clientThreads = new ArrayList<>();
    private volatile boolean shutdown = false;
	
	public WebServer(int port) {
		this.port = port;
	}
	
	public void run() {
		
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(1000);
		} catch (IOException e) {
			System.err.println("I/O error when creating server socket, exiting ...");
			System.exit(1);
		}
		
		while(!shutdown) {
			
			Socket clientSocket = null;
			try {
				
				System.out.println("\nWaiting for connection request ...");
				clientSocket = serverSocket.accept();
				System.out.println("Accepted request for connection.");
				
			} catch (IOException e) {
				System.err.println("I/O error while waiting for connection, please try again.");
			}
			
			try {
				
				inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
				
				System.out.println("Established connection.\n");
				// TODO handle executor
				ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
				WebServerWorker client = new WebServerWorker(clientSocket, inputStream, outputStream);
				clientThreads.add(client);
				client.start();
				
			} catch (SocketTimeoutException e) {
				// TODO handle this exception
			} catch (IOException e) {
				System.err.println("Error establishing connection, please try again.");
			}
			
		}
		
	}
	
	public void shutdown() {
		
	}

}
