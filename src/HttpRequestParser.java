import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Map;

/**
 * Parses HTTP requests and stores the request headers.
 * 
 * @author Raavi Mehta
 * @version 1.0
 */
public class HttpRequestParser {
		
	// Note: Hashtable was used because it is thread-safe
	Hashtable<String, String> requestHeaders = new Hashtable<>();
	String requestLine = null;
	
	/**
	 * Parses the request and stores the request headers.
	 */
	public void parse(String httpRequest) throws IOException, HttpRequestParserException {
		BufferedReader br = new BufferedReader(new StringReader(httpRequest));
		
		// Get the request line
		String line = br.readLine();
		if (line == null || line.length() < 1)
			throw new HttpRequestParserException("Could not parse request line\n");
		this.requestLine = line;
		
		// parse header lines; break when empty line found
		line = br.readLine();
		while (line != null && line.length() > 0) {
			
			int index = line.indexOf(':');
			if (index == -1)
				throw new HttpRequestParserException("The header line \'" + line + "\' was invalid\n");
			
			requestHeaders.put(line.substring(0, index), line.substring(index + 2, line.length()));
			line = br.readLine();
			
		}
	}
	
	public String getRequestLine() {
		return this.requestLine;
	}
	
	public String getHeaderParameter(String header) {
		return requestHeaders.get(header);
	}
	
	/**
	 * Returns the "path" portion of the URL.
	 */
	public String getUrlPath() throws HttpRequestParserException, IOException {
		if (requestLine == null || requestLine.isEmpty())
			throw new HttpRequestParserException("Url null or empty\n");
		
		BufferedReader br = new BufferedReader(new StringReader(requestLine));
		String[] line = br.readLine().split(" ");
		
		// only accept GET requests
		if (!(line.length >= 3) || !line[0].equals("GET"))
			throw new HttpRequestParserException("Improper request line format\n");
		
		return line[1];
	}
	
	/**
	 * Returns contents of Hashtable with request line.
	 */
	public String toString() {
		String st = requestLine + System.lineSeparator();
		for (Map.Entry<String, String> entry : requestHeaders.entrySet())
			st += entry.getKey() + ": " + entry.getValue() + System.lineSeparator();
		return st;
	}
	
}
