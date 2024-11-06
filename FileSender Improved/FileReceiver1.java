import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileReceiver1 extends JFrame {
    private JButton receiveFileButton; // Button to initiate file reception

    public FileReceiver1() {
        super("File Receiver (Localhost)");
        setLayout(new FlowLayout());

        receiveFileButton = new JButton("Receive File");
        add(receiveFileButton);

        // Action listener to prompt for the verification code and start receiving the file
        receiveFileButton.addActionListener(e -> {
            String inputCode = JOptionPane.showInputDialog("Enter the 6-digit code:"); // Prompt for verification code
            receiveFile(inputCode); // Call method to receive file if code matches
        });

        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to receive the file if the entered code matches the sent code
    public void receiveFile(String inputCode) {
        // Define save directory path for received files
        String directoryPath = "YOUR_PATH";// EX : C:\\Users\\JPE\\Desktop\\Received_Files
        File directory = new File(directoryPath);

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (Socket socket = new Socket("localhost", 1234);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String receivedCode = (String) in.readObject(); // Receive the code sent by the sender
            String fileName = (String) in.readObject(); // Receive the file name (with extension)

            // Validate entered code against the received code
            if (!receivedCode.equals(inputCode)) {
                JOptionPane.showMessageDialog(this, "Incorrect code. File transfer canceled.");
                return; // Exit method if code is incorrect
            }

            // Save file with the original file name and extension
            String savePath = directoryPath + "\\" + fileName;
            try (FileOutputStream fileOut = new FileOutputStream(savePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Receive file data and save it to the specified path
                while ((bytesRead = in.read(buffer)) != -1) {
                    fileOut.write(buffer, 0, bytesRead);
                }
            }
            JOptionPane.showMessageDialog(this, "File received and saved to: " + savePath);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error receiving file: " + e.getMessage());
            e.printStackTrace(); // Print error if file reception fails
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileReceiver1::new); // Start the FileReceiver GUI
    }
}
