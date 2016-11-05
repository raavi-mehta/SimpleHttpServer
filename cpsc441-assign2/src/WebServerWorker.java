import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Receieves GET request from client, sends a response, and closes connection.
 * 
 * @author Raavi Mehta
 * @version 1.0
 */
public class WebServerWorker extends Thread {
	
	private Socket clientSocket;
	private BufferedReader inputStream;
	private OutputStream outputStream;
	private HttpRequestParser parser = new HttpRequestParser();

	public WebServerWorker(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.outputStream = clientSocket.getOutputStream();
	}
	
	public void run() {
		
		try {
			
			// Get request and create the response
			getRequest();
			createResponse();
			
		} catch (IOException | HttpRequestParserException e) {
			
			// If connection cannot be parsed, send 400 Bad Request and cleanup
			createBadRequest();
			cleanup();
			
		} catch (Exception e) {
			System.out.println("Error in WebServerWorker:\n" + e.getMessage());
			createBadRequest();
			cleanup();
		}
		
		// close connection and release resources
		cleanup();
		
	}
	
	/** 
	 * Reads and parses request.
	 */
	private void getRequest() throws IOException, HttpRequestParserException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		
		line = inputStream.readLine();
		while(line != null && !line.isEmpty()) {
			sb.append(line);
			line = inputStream.readLine();
		}
		
		parser.parse(sb.toString());
	}
	
	/**
	 * Creates response using parsed request. If file request cannot be found,
	 * send 404 Not Found. Else, send a normal 200 OK response with the file.
	 */
	private void createResponse() throws HttpRequestParserException, IOException {
		
		// Open file
		String path = parser.getUrlPath();
		System.out.println("Requested file path is: " + path);
		File pathFile = new File("." + path);
		
		// If file exists, send 200 OK
		if (pathFile.exists()) {
			System.out.println("Sending response: 200 OK");
			String responseCode = "HTTP/1.1 200 OK\r\n";
			responseCode += "Connection: close\r\n\r\n";
			
			// Read file from filesystem
			FileInputStream fis = new FileInputStream(pathFile);
			byte[] data = new byte[(int) pathFile.length()];
			fis.read(data);
			fis.close();
			
			// send response
			outputStream.write(responseCode.getBytes("US-ASCII"));
			outputStream.write(data);
			outputStream.flush();
		}
		
		// If file does not exist, send 404 Not Found
		else {
			System.out.println("Sending response: 404 Not Found");
			String responseCode = "HTTP/1.1 404 Not Found\r\n";
			responseCode += "Connection: close\r\n\r\n";
			outputStream.write(responseCode.getBytes("US-ASCII"));
			outputStream.flush();
		}
	}
	
	/**
	 * Sends 400 Bad Request if request could not be parsed.
	 */
	private void createBadRequest() {
		System.out.println("Sending response: 400 Bad Request");
		String response = "HTTP/1.1 400 Bad Request\r\n";
		response += "Connection: close\r\n\r\n";
		try {
			outputStream.write(response.getBytes("US-ASCII"));
		} catch (Exception e) {
			System.out.println("Error creating Bad Request:\n" + e.getMessage());
		}
	}
	
	/**
	 * Closes connection and releases resources.
	 */
	private void cleanup() {
		try {
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing client connection:\n" + e.getMessage());
		}
	}
	
}
