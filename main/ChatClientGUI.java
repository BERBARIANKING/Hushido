import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ChatClient client;

    public ChatClientGUI() {
        createGUI();
        connectToServer("192.168.1.200", 1234);  // This should be your server IP if not running locally
        displayAsciiArt();
    }

    private void createGUI() {
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);  // Center the window

        // Using a monospaced font and green text on a black background for the chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(0, 0, 0));
        chatArea.setForeground(new Color(0, 255, 0));
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input field customization
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBackground(new Color(30, 30, 30));
        inputField.setForeground(new Color(0, 255, 0));
        inputField.setCaretColor(new Color(0, 255, 0));

        // Send button customization
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Monospaced", Font.BOLD, 12));
        sendButton.setBackground(new Color(25, 25, 25));
        sendButton.setForeground(new Color(0, 255, 0));
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout(0, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this::sendMessage);
        inputField.addActionListener(this::sendMessage);
    }


  private void displayAsciiArt() {
        String asciiArt =
                "   $$$$$\\                                                                           \n"
                        + "   \\__$$ |                                                                          \n"
                        + "      $$ | $$$$$$\\ $$\\    $$\\ $$$$$$\\                                              \n"
                        + "      $$ | \\____$$\\\\$$\\  $$  |\\____$$\\                                             \n"
                        + "$$\\   $$ | $$$$$$$ |\\$$\\$$  / $$$$$$$ |                                            \n"
                        + "$$ |  $$ |$$  __$$ | \\$$$  / $$  __$$ |                                            \n"
                        + "\\$$$$$$  |\\$$$$$$$ |  \\$  /  \\$$$$$$$ |                                            \n"
                        + " \\______/  \\_______|   \\_/    \\_______|                                            \n"
                        + "                                                                                    \n"
                        + "          $$\\                  $$\\                                                  \n"
                        + "          $$ |                 $$ |                                                 \n"
                        + " $$$$$$$\\ $$$$$$$\\   $$$$$$\\ $$$$$$\\    $$$$$$\\   $$$$$$\\   $$$$$$\\  $$$$$$\\$$$$\\  \n"
                        + "$$  _____|$$  __$$\\  \\____$$\\\\_$$  _|  $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  _$$  _$$\\ \n"
                        + "$$ /      $$ |  $$ | $$$$$$$ | $$ |    $$ |  \\__|$$ /  $$ |$$ /  $$ |$$ / $$ / $$ |\n"
                        + "$$ |      $$ |  $$ |$$  __$$ | $$ |$$\\ $$ |      $$ |  $$ |$$ |  $$ |$$ | $$ | $$ |\n"
                        + "\\$$$$$$$\\ $$ |  $$ |\\$$$$$$$ | \\$$$$  |$$ |      \\$$$$$$  |\\$$$$$$  |$$ | $$ | $$ |\n"
                        + " \\_______|\\__|  \\__| \\_______|  \\____/ \\__|       \\______/  \\______/ \\__| \\__| \\__|\n"
                        + "                                                                                    \n"
                        + "                                                                                    \n"
                        + "                                                                 By Nikolaos Bermparis\n"
                        + "                                                                                     \n";
        chatArea.append(asciiArt);
    }

    private void sendMessage(ActionEvent e) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);
            inputField.setText("");
        }
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            client = new ChatClient(serverAddress, port, this);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error connecting to server: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Set the look and feel to Nimbus before UI components are created
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Handle exception
            }
        }

        EventQueue.invokeLater(() -> new ChatClientGUI().setVisible(true));
    }
}
