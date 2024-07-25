import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class ChatClient {
    private SSLSocket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientGUI gui;

    public ChatClient(String serverAddress, int port, ChatClientGUI gui) throws Exception {
        this.gui = gui;

        // Load the client's truststore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("C:/Users/Berbari/Documents/NetBeansProjects/ChatClient/src/client.truststore"), "password".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        socket = (SSLSocket) socketFactory.createSocket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server: " + socket);

        listenForMessages();
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                String incomingMessage;
                while ((incomingMessage = in.readLine()) != null) {
                    gui.appendMessage(incomingMessage);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                gui.appendMessage("Connection lost.");
            }
        }).start();
    }

    public void sendMessage(String msg) {
        out.println(msg);
        gui.appendMessage("You: " + msg);  // Display your own messages in the chat area
        System.out.println("Sent message: " + msg);
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
            System.out.println("Connection closed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
