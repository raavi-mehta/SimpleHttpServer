import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WebServerWorker extends Thread {
	
	Socket clientSocket;
	Scanner inputStream;
	PrintWriter outputStream;

	public WebServerWorker(Socket clientSocket, Scanner inputStream, PrintWriter outputStream) {
		this.clientSocket = clientSocket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}
	
	public void run() {
		
	}
}
