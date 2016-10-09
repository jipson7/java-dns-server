import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client {

    private static final int port = 6052;
    private static final String host = "127.0.0.1";
    private static String domain;

    public static void main (String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java Client <hostname>");
            System.exit(1);
        }

        domain = args[0];

		Socket s = null;

        try {
            s = new Socket(host, port);
        } catch (IOException e) {
            System.err.format("Unable to connect to %s on port %d.\n", host, port);
            System.exit(1);
        }

		Scanner in = new Scanner(s.getInputStream());
		PrintWriter out = new PrintWriter(s.getOutputStream());

		out.println(domain);
		out.flush();

		System.out.println(in.nextLine());

		out.close();
		in.close();
		s.close();
    }

}
