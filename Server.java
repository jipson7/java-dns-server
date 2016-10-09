import java.net.*;
import java.io.*;
import java.util.HashMap;

public class Server implements Runnable {

    protected static HashMap<String, String> dnsTable;
    private Socket client;

    Server(Socket client) {
        this.client = client;
    }

    private static void initDnsTable() {
        dnsTable = new HashMap<String, String>();
        dnsTable.put("www.google.ca", "10.123.222.2");
        dnsTable.put("www.caleb.ca", "10.211.32.8");
    }

    public static void main(String[] args) throws IOException {

        initDnsTable();

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
                System.out.println("Client Connected.");
                new Thread(new Server(client)).start();
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
            System.exit(1);
        }

        String address, ip;
        try {
            address = in.readLine();
            System.out.format("Client: %s\n", address);
            ip = getIP(address);
            out.println(ip);
            System.out.format("Server: %s\n", ip);
        } catch (IOException e) {
            System.out.println("Read failed.");
            System.exit(1);
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

    private String getIP(String domain) {
        String defaultValue = "Unable to resolve host " + domain + "\n";
        return dnsTable.getOrDefault(domain, defaultValue);
    }
}
