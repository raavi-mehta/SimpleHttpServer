import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebServerWorker extends Thread {
	
	Socket clientSocket;
	BufferedReader inputStream;
	PrintWriter outputStream;

	public WebServerWorker(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.outputStream = new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
	}
	
	public void run() {
		
		String s = null;
		while(true) {
			
			try {
				s = inputStream.readLine();
			} catch (IOException e) {
				outputStream.println("Could not read line");
				break;
			}
			
			// exit if message from client is "bye"
			if(s.equalsIgnoreCase("bye")) {
				outputStream.println("bye");
				outputStream.flush();
				break;
			}
			
			outputStream.println(s);
			outputStream.flush();
			
        }
		
		try {
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing client connection");
		}
		
	}
}
