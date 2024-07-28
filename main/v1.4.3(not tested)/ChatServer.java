import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    private static PrintWriter logWriter;

    public static void main(String[] args) {
        try {
            // Initialize log file writer
            logWriter = new PrintWriter(new FileWriter("chat_server_log.txt", true), true);

            // Load the server's keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("C:/Users/Berbari/Documents/NetBeansProjects/ChatServer/src/server.keystore"), "password".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "password".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(1234);
            System.out.println("Secure server is running and waiting for connections.");
            logWriter.println("Secure server is running and waiting for connections.");

            while (true) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                logWriter.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }
        } catch (Exception e) {
            System.out.println("Error when initializing server: " + e.getMessage());
            logWriter.println("Error when initializing server: " + e.getMessage());
            e.printStackTrace(logWriter);
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
        logWriter.println(message);
    }

    private static class ClientHandler implements Runnable {
        private SSLSocket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(SSLSocket socket) {
            this.clientSocket = socket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Streams set up successfully for: " + clientSocket);
                logWriter.println("Streams set up successfully for: " + clientSocket);
            } catch (IOException e) {
                System.out.println("Error setting up streams: " + e.getMessage());
                logWriter.println("Error setting up streams: " + e.getMessage());
                e.printStackTrace(logWriter);
            }
        }

        @Override
        public void run() {
            try {
                // Read the username sent by the client
                username = in.readLine();
                System.out.println("User " + username + " connected.");
                logWriter.println("User " + username + " connected.");
                sendMessage("Welcome to the chat, " + username + "!");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String formattedMessage = "[" + username + "]: " + inputLine;
                    System.out.println(formattedMessage);
                    logWriter.println(formattedMessage);
                    broadcast(formattedMessage, this);
                }
                System.out.println("Client disconnected: " + clientSocket);
                logWriter.println("Client disconnected: " + clientSocket);
                clients.remove(this);
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error handling client " + clientSocket + ": " + e.getMessage());
                logWriter.println("Error handling client " + clientSocket + ": " + e.getMessage());
                clients.remove(this);
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println("Error closing socket for client " + clientSocket + ": " + ex.getMessage());
                    logWriter.println("Error closing socket for client " + clientSocket + ": " + ex.getMessage());
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
            System.out.println(message);
            logWriter.println(message);
        }
    }
}
