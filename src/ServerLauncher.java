import java.util.Scanner;

public class ServerLauncher implements Constants {
	
	public static void main(String[] args) {
		int serverPort = PORT;

		if (args.length == 1) {
			serverPort = Integer.parseInt(args[0]);
		}

		System.out.println("Starting the server on port " + serverPort);

		WebServer server = new WebServer(serverPort);

		server.start();
		System.out.println("Server started. Type \"quit\" to stop");
		System.out.println(".....................................");

		Scanner keyboard = new Scanner(System.in);
		while ( !keyboard.next().equalsIgnoreCase("quit"));

		System.out.println();
		server.shutdown();
		
		try {
			server.join();
		} catch (InterruptedException e) {
			// Do nothing since server has exited as required
		}
		
		System.out.println("Server stopped.");
		keyboard.close();
	}

}
