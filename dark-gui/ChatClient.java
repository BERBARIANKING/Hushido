import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientGUI gui;

    public ChatClient(String serverAddress, int port, ChatClientGUI gui) throws IOException {
        this.gui = gui;
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
