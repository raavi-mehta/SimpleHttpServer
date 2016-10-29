import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A Simple Client
 */
public class TCPClient implements Constants {

	public static void main(String[] args) {

		String s, tmp;
		Scanner inputStream;
		PrintWriter outputStream;
		Scanner userinput;

		try {
			// connects to port server app listesing at port 8888 in the same
			// machine
			Socket socket = new Socket(HOSTNAME, PORT);

			// Create necessary streams
			outputStream = new PrintWriter(new DataOutputStream(
					socket.getOutputStream()));
			inputStream = new Scanner(new InputStreamReader(
					socket.getInputStream()));
			userinput = new Scanner(System.in);

			// send/receive messages to/from server
			while (true) {
				System.out.println("Enter Text Message for Echo Server: ");
				tmp = userinput.nextLine();

				// Send user input message to server
				outputStream.println(tmp);
				// Flush to make sure message is send
				outputStream.flush();

				s = inputStream.nextLine();
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
