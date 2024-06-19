import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class VoiceChatClient {

    public static void main(String[] args) {
        try {
            final Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server");

            // Setup audio format
            final AudioFormat format = new AudioFormat(16000, 16, 2, true, true);
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
            DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

            // Setup microphone line for capture
            final TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(targetInfo);
            microphone.open(format);
            microphone.start();

            // Setup speakers for playback
            final SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            speakers.open(format);
            speakers.start();

            // Get the input and output streams for the socket connection
            final OutputStream output = socket.getOutputStream();
            final InputStream input = socket.getInputStream();

            // Create a thread for capturing and sending microphone data
            Thread captureThread = new Thread(() -> {
                byte[] buffer = new byte[1024];
                int numBytesRead;
                try {
                    while (!Thread.interrupted()) {
                        numBytesRead = microphone.read(buffer, 0, buffer.length);
                        output.write(buffer, 0, numBytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    microphone.close();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Create a thread for receiving and playing back audio from the server
            Thread playbackThread = new Thread(() -> {
                byte[] buffer = new byte[1024];
                int numBytesRead;
                try {
                    while (!Thread.interrupted()) {
                        numBytesRead = input.read(buffer, 0, buffer.length);
                        if (numBytesRead > 0) {
                            speakers.write(buffer, 0, numBytesRead);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    speakers.close();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            captureThread.start();
            playbackThread.start();

            // Wait for the threads to finish
            captureThread.join();
            playbackThread.join();
        } catch (LineUnavailableException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
