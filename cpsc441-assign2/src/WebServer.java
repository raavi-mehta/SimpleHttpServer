import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebServer extends Thread {
	
	private int port;
	private Scanner inputStream;
    private PrintWriter outputStream;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private volatile boolean shutdown = false;
	
	public WebServer(int port) {
		this.port = port;
	}
	
	public void run() {
		
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(1000);
		} catch (IOException e) {
			System.out.println("I/O error when creating server socket:\n" + e.getMessage() + "\nExiting ...");
			System.exit(1);
		}
		
		System.out.println("\nWaiting for connection requests ...\n");
		while(!shutdown) {
			
			Socket clientSocket = null;
			try {
				
				clientSocket = serverSocket.accept();
				System.out.println("Accepted request for connection.");
				
				inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
				outputStream = new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
				
				System.out.println("Established connection.\n");
				executor.execute(new WebServerWorker(clientSocket, inputStream, outputStream));
				
			} catch (SocketTimeoutException e) {
				// TODO handle if needed
				
			} catch (IOException e) {
				System.out.println("I/O error while creating connection:\n" + e.getMessage());
			}
			
		}
		cleanup();
		
	}
	
	public void shutdown() {
		this.shutdown = true;
	}
	
	private void cleanup() {
		
		// terminate all threads
		try {
			executor.shutdown();
			
			 if (!executor.awaitTermination(5, TimeUnit.SECONDS)) { 
				 executor.shutdownNow();
			 }
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
		
		// release resources
		try {
			inputStream.close();
			outputStream.close();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error shutting down server:\n" + e.getMessage());
		}
	}

}
