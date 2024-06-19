import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);  // Server listening on port 5000
        System.out.println("Server is listening...");
        
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");

        InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);  // Echo the received data back to the client
        }

        serverSocket.close();
        clientSocket.close();
    }
}
