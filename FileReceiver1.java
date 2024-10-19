import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileReceiver1 extends JFrame {
    private JButton receiveFileButton;

    public FileReceiver1() {
        super("File Receiver (Localhost)");

        setLayout(new FlowLayout());

        receiveFileButton = new JButton("Receive File");
        add(receiveFileButton);

        receiveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiveFile(); // Call the updated method
            }
        });

        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void receiveFile() {
        // Set save path to specified directory
        String directoryPath = "C:\\Users\\bhara\\OneDrive\\Desktop\\Received file";
        File directory = new File(directoryPath);

        // Create directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String savePath = directoryPath + "\\received_file.png";

        try (Socket socket = new Socket("localhost", 1234); // Fixed port 1234
             BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             FileOutputStream fileOut = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
            JOptionPane.showMessageDialog(this, "File received and saved to: " + savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new FileReceiver1();
    }
}
