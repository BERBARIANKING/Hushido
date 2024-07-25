import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ChatClient client;
    private String username;
    private String password;
    private String serverAddress;

    public ChatClientGUI() {
        showMainMenu();
    }

    private void showMainMenu() {
        JFrame mainMenu = new JFrame("Chat Client - Main Menu");
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize(600, 800);
        mainMenu.setLayout(new BorderLayout());
        mainMenu.getContentPane().setBackground(Color.BLACK);

        // Add a 90s-2000s style logo or image at the top
        JLabel logo = new JLabel(new ImageIcon("C:\\Users\\Berbari\\Documents\\NetBeansProjects\\ChatClient\\src\\LOGO_1.png")); // Add your image path here
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        mainMenu.add(logo, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);

        JButton connectButton = new JButton("Connect");
        JButton aboutButton = new JButton("About");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        connectButton.setFont(new Font("Courier", Font.PLAIN, 14));
        aboutButton.setFont(new Font("Courier", Font.PLAIN, 14));
        registerButton.setFont(new Font("Courier", Font.PLAIN, 14));
        exitButton.setFont(new Font("Courier", Font.PLAIN, 14));

        connectButton.addActionListener(e -> {
            mainMenu.dispose();
            showServerInputDialog();
        });

        aboutButton.addActionListener(e -> showAboutDialog());

        registerButton.addActionListener(e -> showRegisterDialog());

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(connectButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        mainMenu.add(buttonPanel, BorderLayout.CENTER);

        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }

    private void showServerInputDialog() {
        serverAddress = JOptionPane.showInputDialog(this, "Enter server IP address:", "Server IP", JOptionPane.PLAIN_MESSAGE);
        if (serverAddress != null && !serverAddress.trim().isEmpty()) {
            showLoginDialog();
        } else {
            showErrorDialog("Invalid IP address. Please enter a valid IP address.");
            showMainMenu();
        }
    }

    private void showLoginDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Username"),
                usernameField,
                new JLabel("Password"),
                passwordField
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            username = usernameField.getText();
            password = new String(passwordField.getPassword());
            if (authenticate(username, password)) {
                try {
                    connectToServer(serverAddress, 1234);
                    createGUI();
                    displayAsciiArt();
                } catch (IOException e) {
                    showErrorDialog("Failed to connect to server. Please check the IP address and try again.");
                    showServerInputDialog();
                } catch (Exception e) {
                    showErrorDialog("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Authentication failed", "Error", JOptionPane.ERROR_MESSAGE);
                showLoginDialog();
            }
        } else {
            showMainMenu();
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "Chat Client\nVersion 1.0\nBy Nikolaos Bermparis", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRegisterDialog() {
        JOptionPane.showMessageDialog(this, "Registration feature is not implemented yet.", "Register", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean authenticate(String username, String password) {
        // Replace with real authentication logic
        if (username.equals("root") && password.equals("rootpass")) {
            return true;
        }
        return !username.isEmpty() && password.equals("password");
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

    private void connectToServer(String serverAddress, int port) throws Exception {
        client = new ChatClient(serverAddress, port, this);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
