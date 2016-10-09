import java.net.*;
import java.io.*;

public class Server implements Runnable {

    Socket client;
    int clientNumber;
    Server(Socket client, int clientNumber) {
        this.client = client;
        this.clientNumber = clientNumber;
    }

    public static void main(String[] args) throws IOException {

        int clientCount = 0;
        int port = 6052;

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.format("Connection opened, listening on port: %d.\n", port);
        } catch (IOException e) {
            System.err.format("Could not listen on port: %d.\n", port);
            System.exit(1);
        }

        while(true) {
            try {
                Socket client = server.accept();
                clientCount++;
                System.out.format("Client %d Connected.\n", clientCount);
                new Thread(new Server(client, clientCount)).start();
            } catch (IOException e) {
                System.err.format("Accept failed: %d.\n", port);
                System.exit(1);
            }
        }
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
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
                System.out.format("Client %d: %s\n", clientNumber, line);
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
        try {
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.err.println("Error closing client");
            System.exit(1);
        }
    }
}
