import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A simple HTTP server that accepts clients and creates WebServerWorkers to
 * deal with their GET requests.
 * 
 * @author Raavi Mehta
 * @version 1.0
 */
public class WebServer extends Thread {
	
	private int port;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private volatile boolean shutdown = false;
	
	public WebServer(int port) {
		this.port = port;
	}
	
	public void run() {
		
		// Create server instance
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(1000);
		} catch (IOException e) {
			System.out.println("I/O error when creating server socket:\n" + e.getMessage() + "\nExiting ...");
			System.exit(1);
		}
		
		// Accept connections, and send workers to handle requests
		System.out.println("\nWaiting for connection requests ...\n");
		while(!shutdown) {
			
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("\nAccepted request for connection.");
				executor.execute(new WebServerWorker(clientSocket));
			} catch (SocketTimeoutException e) {
				// Do nothing
				
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
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error shutting down server:\n" + e.getMessage());
		}
	}

}
