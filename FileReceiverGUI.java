import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class FileReceiverGUI {
    private JFrame frame;
    private JButton receiveFileButton;
    private JLabel statusLabel;
    private JTextField ipField;

    public FileReceiverGUI() {
        // Create the main frame
        frame = new JFrame("File Receiver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());

        // Create a panel for the main content
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and style the IP label and text field
        JLabel ipLabel = new JLabel("Enter Server IP:");
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        ipField = new JTextField(15);
        ipField.setText("192.168.28.158");
        ipField.setMaximumSize(new Dimension(Integer.MAX_VALUE, ipField.getPreferredSize().height));

        // Create and style the receive file button
        receiveFileButton = new JButton("Receive File");
        receiveFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiveFileButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Create and style the status label
        statusLabel = new JLabel("Waiting to receive file...");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add components to the panel
        panel.add(ipLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(ipField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(receiveFileButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statusLabel);

        // Add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Button listener for receiving file
        receiveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiveFile();
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private void receiveFile() {
        Socket socket = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;

        try {
            // Create a file chooser to allow the user to select where to save the file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Save Location");
            fileChooser.setSelectedFile(new File("received_file.txt"));  // Default file name

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();  // Get selected file path

                // Get IP address from the text field
                String serverIP = ipField.getText().trim();
                if (serverIP.isEmpty()) {
                    statusLabel.setText("Please enter a valid IP address.");
                    return;
                }

                // Connect to the server at the given IP on port 5000
                socket = new Socket(serverIP, 5000);  // Use entered IP address
                statusLabel.setText("Connected to server. Receiving file...");

                // Create a file output stream to write the received data to the selected file
                fileOutputStream = new FileOutputStream(saveFile);
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());

                // Buffer to read data
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                statusLabel.setText("File received successfully! Saved to: " + saveFile.getAbsolutePath());
            } else {
                statusLabel.setText("File save cancelled.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to receive file.");
        } finally {
            try {
                // Close all streams and sockets
                if (fileOutputStream != null) fileOutputStream.close();
                if (bufferedInputStream != null) bufferedInputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new FileReceiverGUI();  // Launch the GUI
    }
}
