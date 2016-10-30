import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

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
			getRequest();
			createResponse();
		} catch (IOException | HttpRequestParserException e) {
			createBadRequest();
			cleanup();
		} catch (Exception e) {
			System.out.println("Could not process request");
			e.getStackTrace();
		}
		
		cleanup();
		
	}
	
	private void getRequest() throws IOException, HttpRequestParserException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		
		line = inputStream.readLine();
		while(line != null && !line.isEmpty()) {
			sb.append(line + "\r\n");
			line = inputStream.readLine();
		}
		
		parser.parse(sb.toString());
	}
	
	private void createResponse() throws HttpRequestParserException, IOException {
		String path = parser.getUrlPath().substring(1);
		System.out.println("Requested file path is: " + path);
		File pathFile = new File(path);
		
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
		else {
			System.out.println("Sending response: 404 Not Found");
			String responseCode = "HTTP/1.1 404 Not Found\r\n";
			responseCode += "Connection: close\r\n\r\n";
			outputStream.write(responseCode.getBytes("US-ASCII"));
			outputStream.flush();
		}
	}
	
	private void createBadRequest() {
		System.out.println("Sending response: 400 Bad request");
		String response = "HTTP/1.1 400 Bad Request\r\n";
		response += "Connection: close\r\n\r\n";
		try {
			outputStream.write(response.getBytes("US-ASCII"));
		} catch (Exception e) {
			System.out.println("Could not write response to socket:");
			e.printStackTrace();
		}
	}
	
	private void cleanup() {
		try {
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error closing client connection");
		}
	}
	
}
