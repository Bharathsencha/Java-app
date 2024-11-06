import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FileSender1 extends JFrame {
    private JButton selectFileButton; // Button to select and send file
    private File selectedFile; // Selected file to send
    private String generatedCode; // 6-digit code for verification

    public FileSender1() {
        super("File Sender (Localhost)");

        setLayout(new FlowLayout());
        selectFileButton = new JButton("Select File and Send");
        add(selectFileButton);

        // Action listener for button to select file and send it
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                generatedCode = generateCode(); // Generate 6-digit verification code
                JOptionPane.showMessageDialog(this, "Generated Code: " + generatedCode); // Display code to user
                sendFile(); // Call method to send file
            }
        });

        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to generate a random 6-digit code for verification
    private String generateCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000)); // Generate a 6-digit random number as a string
    }

    // Method to send the file and verification code over a socket connection
    public void sendFile() {
        try (ServerSocket serverSocket = new ServerSocket(1234);
             Socket socket = serverSocket.accept();
             FileInputStream fileIn = new FileInputStream(selectedFile);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            out.writeObject(generatedCode); // Send verification code to receiver
            out.writeObject(selectedFile.getName()); // Send file name for saving with correct extension

            // Send file data in chunks
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush(); // Ensure all data is sent
            JOptionPane.showMessageDialog(this, "File sent successfully!");
        } catch (IOException e) {
            e.printStackTrace(); // Print error if file transfer fails
        }
    }

    public static void main(String[] args) {
        new FileSender1(); // Launch the FileSender GUI
    }
}
