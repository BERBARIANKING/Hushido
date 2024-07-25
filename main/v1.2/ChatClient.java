import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientGUI gui;
    private String username;
    private String password;

    public ChatClient(String serverAddress, int port, ChatClientGUI gui, String username, String password) throws IOException {
        this.gui = gui;
        this.username = username;
        this.password = password;
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(username); // Send username to the server
        out.println(password); // Send password to the server
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
        gui.appendMessage("You: " + msg); // Display your own messages in the chat area
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
