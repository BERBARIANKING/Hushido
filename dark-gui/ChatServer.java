import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;




public class ChatServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static ExecutorService pool = Executors.newFixedThreadPool(10);  // Adjust thread pool size based on expected client load

    public static void main(String[] args) {
        
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server is running and waiting for connections..");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                pool.execute(clientHandler);  // Use the pool to manage threads
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
                String username = getUsername();
                System.out.println("User " + username + " connected.");

                out.println("Welcome to the chat, " + username + "!");
                out.println("Type Your Message");
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

        private String getUsername() throws IOException {
            out.println("Enter your username:");
            return in.readLine();
        }

        public void sendMessage(String message) {
            out.println(message);
            out.println("Type Your Message");
        }
    }
}
