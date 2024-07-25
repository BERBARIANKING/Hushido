import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "rootpass";
    private static final String DEFAULT_PASSWORD = "password";

    public static void main(String[] args) {
        try {
            String host = "192.168.1.88"; // Replace with your server's IP address
            int port = 1234;
            InetAddress address = InetAddress.getByName(host);
            ServerSocket serverSocket = new ServerSocket(port, 50, address);
            System.out.println("Server is running and waiting for connections on " + host + ":" + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.out.println("Error when initializing server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                System.out.println("Error setting up streams: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                if (!authenticate()) {
                    out.println("Authentication failed. Disconnecting...");
                    clientSocket.close();
                    return;
                }

                System.out.println("User " + username + " connected.");
                out.println("Welcome to the chat, " + username + "!");
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("[" + username + "]: " + inputLine);
                    broadcast("[" + username + "]: " + inputLine, this);
                }
                clients.remove(this);
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error handling client " + clientSocket + ": " + e.getMessage());
                clients.remove(this);
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("Error closing socket for client " + clientSocket + ": " + ex.getMessage());
                }
            }
        }

        private boolean authenticate() throws IOException {
            username = in.readLine(); // Read the username sent by the client
            String password = in.readLine(); // Read the password sent by the client

            if (username.equals(ROOT_USERNAME) && password.equals(ROOT_PASSWORD)) {
                return true;
            }

            // Default credentials check
            if (!username.isEmpty() && password.equals(DEFAULT_PASSWORD)) {
                return true;
            }

            return false;
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
