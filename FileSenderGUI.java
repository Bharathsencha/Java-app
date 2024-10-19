import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class FileSenderGUI {
    private JFrame frame;
    private JButton selectFileButton;
    private JLabel statusLabel;
    private File selectedFile;

    public FileSenderGUI() {
        // Create the main frame
        frame = new JFrame("File Sender");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        // Create a panel for the main content
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and style the select file button
        selectFileButton = new JButton("Select File to Send");
        selectFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectFileButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Create and style the status label
        statusLabel = new JLabel("No file selected.");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Add components to the panel
        panel.add(selectFileButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(statusLabel);

        // Add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Button listener for file selection
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser dialog
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // If a file is selected, update the status label and send the file
                    selectedFile = fileChooser.getSelectedFile();
                    statusLabel.setText("Selected file: " + selectedFile.getName());
                    sendFile(selectedFile);
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private void sendFile(File file) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        FileInputStream fileInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // Start the server on port 5000, listening on all network interfaces
            serverSocket = new ServerSocket(5000, 50, InetAddress.getByName("192.168.7.26"));
            statusLabel.setText("Waiting for client connection...");

            // Accept client connection
            socket = serverSocket.accept();
            statusLabel.setText("Client connected. Sending file...");

            // Read file and send over the socket
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

            int bytesRead = fileInputStream.read(fileBytes, 0, fileBytes.length);
            bufferedOutputStream.write(fileBytes, 0, bytesRead);
            bufferedOutputStream.flush();
            statusLabel.setText("File sent successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to send file.");
        } finally {
            try {
                // Close all streams and sockets
                if (fileInputStream != null) fileInputStream.close();
                if (bufferedOutputStream != null) bufferedOutputStream.close();
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new FileSenderGUI();  // Launch the GUI
    }
}
