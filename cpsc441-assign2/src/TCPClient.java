import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A Simple Client
 */
public class TCPClient implements Constants {

	public static void main(String[] args) {

		String s, tmp;
		BufferedReader inputStream;
		PrintWriter outputStream;
		BufferedReader userinput;

		try {
			// connects to port server app listesing at port 8888 in the same
			// machine
			Socket socket = new Socket(HOSTNAME, PORT);

			// Create necessary streams
			outputStream = new PrintWriter(new DataOutputStream(
					socket.getOutputStream()));
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userinput = new BufferedReader(new InputStreamReader(System.in));

			// send/receive messages to/from server
			while (true) {
				System.out.println("Enter Text Message for Echo Server: ");
				tmp = userinput.readLine();

				// Send user input message to server
				outputStream.println(tmp);
				// Flush to make sure message is send
				outputStream.flush();

				s = inputStream.readLine();
				System.out.println(s);

				// Exit if message from server is "bye"
				if (s.equalsIgnoreCase("bye"))
					break;

			}
			inputStream.close();
			outputStream.close();
			socket.close();
		}
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
