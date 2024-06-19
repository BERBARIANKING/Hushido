import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class VoiceChatClient {
    public static void main(String[] args) throws LineUnavailableException, IOException {
        Socket socket = new Socket("localhost", 5000);  // Connect to the server
        System.out.println("Connected to server");

        // Audio format parameters
        AudioFormat format = new AudioFormat(16000, 16, 2, true, true);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);


        // Open and start the microphone line
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        // Output stream to send data to server
        OutputStream output = socket.getOutputStream();

        // Input stream to receive data from server
        InputStream input = socket.getInputStream();

        // Buffer for reading and writing data
        byte[] buffer = new byte[1024];
        int numBytesRead;

try {
    while (true) {
        numBytesRead = microphone.read(buffer, 0, buffer.length);
        output.write(buffer, 0, numBytesRead);
    }
} finally {
    // This block will execute even if the loop exits due to an exception
    microphone.close();
    socket.close();
}


    }
}
