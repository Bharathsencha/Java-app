import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileSender1 extends JFrame {
    private JButton selectFileButton;
    private File selectedFile;

    public FileSender1() {
        super("File Sender (Localhost)");

        setLayout(new FlowLayout());

        selectFileButton = new JButton("Select File and Send");
        add(selectFileButton);

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    sendFile();
                }
            }
        });

        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void sendFile() {
        try (ServerSocket serverSocket = new ServerSocket(1234);  // Fixed port 1234
             Socket socket = serverSocket.accept();
             FileInputStream fileIn = new FileInputStream(selectedFile);
             BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            JOptionPane.showMessageDialog(this, "File sent successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileSender1();
    }
}
