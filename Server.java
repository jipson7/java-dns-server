import java.net.*;
import java.io.*;

public class Server {

    private static final int port = 6052;

    private static ServerSocket server = null;
    private static Socket client = null;
    private static BufferedReader in = null;
    private static PrintWriter out = null;

    public static void main(String[] args) throws IOException {

        try {
            server = new ServerSocket(port);
            System.out.format("Connection opened, listening on port: %d.\n", port);
        } catch (IOException e) {
            System.err.format("Could not listen on port: %d.\n", port);
            System.exit(1);
        }

        try {
            client = server.accept();
        } catch (IOException e) {
            System.err.format("Accept failed: %d.\n", port);
            System.exit(1);
        }

        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Read failed.");
            System.exit(-1);
        }

        String line;
        while(true) {
            try {
                line = in.readLine();
                System.out.format("Client: %s\n", line);
                if (line.toLowerCase().equals("bye")) {
                    out.println("Shutting down.");
                    break;
                }
                out.println(line);
                System.out.format("Server: %s\n", line);
            } catch (IOException e) {
                System.out.println("Read failed.");
                System.exit(-1);
            }

        }

        out.close();
        in.close();
        client.close();
        server.close();
    }
}
