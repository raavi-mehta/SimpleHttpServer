import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HttpRequestParserTester {
	public static void main(String[] args) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader("request.txt"))) {
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		}
		
		String request = sb.toString();
		HttpRequestParser parser = new HttpRequestParser();
		try {
			parser.parse(request);
		} catch (HttpRequestParserException e) {
			e.printStackTrace();
		}
		
		System.out.println("Request was ...\n\n" + parser.toString());
	}
}
