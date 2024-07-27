import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.border.Border;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Add a 90s-2000s style logo or image at the top
        JLabel logo = new JLabel(new ImageIcon("C:\\Users\\Berbari\\Documents\\NetBeansProjects\\ChatClient\\src\\LOGO_1.png"));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        add(logo, BorderLayout.NORTH);

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

        connectButton.addActionListener(e -> showServerInputDialog());
        aboutButton.addActionListener(e -> showAboutDialog());
        registerButton.addActionListener(e -> showRegisterDialog());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(connectButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showServerInputDialog() {
        serverAddress = JOptionPane.showInputDialog(this, "Enter server IP address:", "Server IP", JOptionPane.PLAIN_MESSAGE);
        if (serverAddress != null && !serverAddress.trim().isEmpty()) {
            showLoginDialog();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid IP address. Please enter a valid IP address.", "Error", JOptionPane.ERROR_MESSAGE);
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
        int result = JOptionPane.showConfirmDialog(this, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);
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
        }
    }

    private void createGUI() {
        getContentPane().removeAll();
        getContentPane().repaint();
        getContentPane().revalidate();

        setTitle("Chat Client");
        setSize(800, 500);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.GREEN);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.GREEN);
        inputField.setCaretColor(Color.GREEN);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Monospaced", Font.BOLD, 12));
        sendButton.setBackground(Color.GRAY);
        sendButton.setForeground(Color.GREEN);
        sendButton.addActionListener(this::sendMessage);

        inputField.addActionListener(this::sendMessage);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void displayAsciiArt() {
        String asciiArt =
                "   $$$$$\\                                                                           \n" +
                        "   \\__$$ |                                                                          \n" +
                        "      $$ | $$$$$$\\ $$\\    $$\\ $$$$$$\\                                              \n" +
                        "      $$ | \\____$$\\\\$$\\  $$  |\\____$$\\                                             \n" +
                        "$$\\   $$ | $$$$$$$ |\\$$\\$$  / $$$$$$$ |                                            \n" +
                        "$$ |  $$ |$$  __$$ | \\$$$  / $$  __$$ |                                            \n" +
                        "\\$$$$$$  |\\$$$$$$$ |  \\$  /  \\$$$$$$$ |                                            \n" +
                        " \\______/  \\_______|   \\_/    \\_______|                                            \n" +
                        "                                                                                    \n" +
                        "          $$\\                  $$\\                                                  \n" +
                        "          $$ |                 $$ |                                                 \n" +
                        " $$$$$$$\\ $$$$$$$\\   $$$$$$\\ $$$$$$\\    $$$$$$\\   $$$$$$\\   $$$$$$\\  $$$$$$\\$$$$\\  \n" +
                        "$$  _____|$$  __$$\\  \\____$$\\\\_$$  _|  $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  _$$  _$$\\ \n" +
                        "$$ /      $$ |  $$ | $$$$$$$ | $$ |    $$ |  \\__|$$ /  $$ |$$ /  $$ |$$ / $$ / $$ |\n" +
                        "$$ |      $$ |  $$ |$$  __$$ | $$ |$$\\ $$ |      $$ |  $$ |$$ |  $$ |$$ | $$ | $$ |\n" +
                        "\\$$$$$$$\\ $$ |  $$ |\\$$$$$$$ | \\$$$$  |$$ |      \\$$$$$$  |\\$$$$$$  |$$ | $$ | $$ |\n" +
                        " \\_______|\\__|  \\__| \\_______|  \\____/ \\__|       \\______/  \\______/ \\__| \\__| \\__|\n" +
                        "                                                                                    \n" +
                        "                                                                                    \n" +
                        "                                                                 By Nikolaos Bermparis\n" +
                        "                                                                                     \n";
        chatArea.append(asciiArt);
    }

    private void sendMessage(ActionEvent e) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(username + ": " + message);
            inputField.setText("");
        }
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private boolean authenticate(String username, String password) {
        return !username.isEmpty() && password.equals("password");
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About Hushido", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setSize(new Dimension(1200, 800));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        JLabel textLabel1 = new JLabel("<html><div style='text-align: center;'>" +
                "Hushido<br>Version 1.3<br>By Nikolaos Bermparis</div></html>");
        textLabel1.setBorder(margin);
        panel.add(textLabel1, gbc);

        ImageIcon image1 = new ImageIcon("C:\\Users\\Berbari\\Documents\\NetBeansProjects\\ChatClient\\src\\samurai.jpg");
        JLabel label1 = new JLabel(image1, SwingConstants.CENTER);
        label1.setBorder(margin);
        panel.add(label1, gbc);

        JLabel textLabel2 = new JLabel("<html><div style='text-align: center;'>" +
                "The name Hushido from the word Bushidō (武士道, 'the way of the warrior') is a moral code concerning samurai attitudes, behavior, and lifestyle, formalized in the Edo period (1603–1868)." +
                "<br><br>A play on 'Bushido', which is the samurai code of honor, combined with 'hush'. A secure and encrypted chatroom made in Java." +
                "<br><br>The application is made by Nikolaos Bermparis under the Apache-2.0 license.</div></html>");
        textLabel2.setBorder(margin);
        panel.add(textLabel2, gbc);

        ImageIcon image2 = new ImageIcon("C:\\Users\\Berbari\\Documents\\NetBeansProjects\\ChatClient\\src\\creator.jpg");
        JLabel label2 = new JLabel(image2, SwingConstants.CENTER);
        label2.setBorder(margin);
        panel.add(label2, gbc);

        JTextArea licenseText = new JTextArea(
                "Apache License\n" +
                        "Version 2.0, January 2004\n" +
                        "http://www.apache.org/licenses/\n\n" +
                        "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n\n" +
                        "1. Definitions.\n\n" +
                        "   \"License\" shall mean the terms and conditions for use, reproduction,\n" +
                        "   and distribution as defined by Sections 1 through 9 of this document.\n\n" +
                        "   \"Licensor\" shall mean the copyright owner or entity authorized by\n" +
                        "   the copyright owner that is granting the License.\n\n" +
                        "   \"Legal Entity\" shall mean the union of the acting entity and all\n" +
                        "   other entities that control, are controlled by, or are under common\n" +
                        "   control with that entity. For the purposes of this definition,\n" +
                        "   \"control\" means (i) the power, direct or indirect, to cause the\n" +
                        "   direction or management of such entity, whether by contract or\n" +
                        "   otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
                        "   outstanding shares, or (iii) beneficial ownership of such entity.\n\n" +
                        "   \"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
                        "   exercising permissions granted by this License.\n\n" +
                        "   \"Source\" form shall mean the preferred form for making modifications,\n" +
                        "   including but not limited to software source code, documentation source,\n" +
                        "   and configuration files.\n\n" +
                        "   \"Object\" form shall mean any form resulting from mechanical\n" +
                        "   transformation or translation of a Source form, including but\n" +
                        "   not limited to compiled object code, generated documentation,\n" +
                        "   and conversions to other media types.\n\n" +
                        "   \"Work\" shall mean the work of authorship, whether in Source or\n" +
                        "   Object form, made available under the License, as indicated by a\n" +
                        "   copyright notice that is included in or attached to the work\n" +
                        "   (an example is provided in the Appendix below).\n\n" +
                        "   \"Derivative Works\" shall mean any work, whether in Source or Object\n" +
                        "   form, that is based on (or derived from) the Work and for which the\n" +
                        "   editorial revisions, annotations, elaborations, or other modifications\n" +
                        "   represent, as a whole, an original work of authorship. For the purposes\n" +
                        "   of this License, Derivative Works shall not include works that remain\n" +
                        "   separable from, or merely link (or bind by name) to the interfaces of,\n" +
                        "   the Work and Derivative Works thereof.\n\n" +
                        "   \"Contribution\" shall mean any work of authorship, including\n" +
                        "   the original version of the Work and any modifications or additions\n" +
                        "   to that Work or Derivative Works thereof, that is intentionally\n" +
                        "   submitted to Licensor for inclusion in the Work by the copyright owner\n" +
                        "   or by an individual or Legal Entity authorized to submit on behalf of\n" +
                        "   the copyright owner. For the purposes of this definition, \"submitted\"\n" +
                        "   means any form of electronic, verbal, or written communication sent\n" +
                        "   to the Licensor or its representatives, including but not limited to\n" +
                        "   communication on electronic mailing lists, source code control systems,\n" +
                        "   and issue tracking systems that are managed by, or on behalf of, the\n" +
                        "   Licensor for the purpose of discussing and improving the Work, but\n" +
                        "   excluding communication that is conspicuously marked or otherwise\n" +
                        "   designated in writing by the copyright owner as \"Not a Contribution.\"\n\n" +
                        "   \"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
                        "   on behalf of whom a Contribution has been received by Licensor and\n" +
                        "   subsequently incorporated within the Work.\n\n" +
                        "2. Grant of Copyright License. Subject to the terms and conditions of\n" +
                        "   this License, each Contributor hereby grants to You a perpetual,\n" +
                        "   worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
                        "   copyright license to reproduce, prepare Derivative Works of,\n" +
                        "   publicly display, publicly perform, sublicense, and distribute the\n" +
                        "   Work and such Derivative Works in Source or Object form.\n\n" +
                        "   END OF TERMS AND CONDITIONS\n" +
                        "   Copyright 17/6/2024 Bermparis Nikolaos\n\n" +
                        "   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "   you may not use this file except in compliance with the License.\n" +
                        "   You may obtain a copy of the License at\n\n" +
                        "       http://www.apache.org/licenses/LICENSE-2.0\n\n" +
                        "   Unless required by applicable law or agreed to in writing, software\n" +
                        "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "   See the License for the specific language governing permissions and\n" +
                        "   limitations under the License."
        );
        licenseText.setEditable(false);
        licenseText.setLineWrap(true);
        licenseText.setWrapStyleWord(true);
        licenseText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane textScrollPane = new JScrollPane(licenseText);
        textScrollPane.setPreferredSize(new Dimension(400, 200));
        textScrollPane.setBorder(margin);
        panel.add(textScrollPane, gbc);

        JScrollPane mainScrollPane = new JScrollPane(panel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        dialog.add(mainScrollPane);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showRegisterDialog() {
        JOptionPane.showMessageDialog(this, "Registration feature is not implemented yet.", "Register", JOptionPane.INFORMATION_MESSAGE);
    }

    private void connectToServer(String serverAddress, int port) throws Exception {
        client = new ChatClient(serverAddress, port, this);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
