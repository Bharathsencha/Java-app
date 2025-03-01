# Java File Transfer Application

## Overview
This is a simple Java application consisting of two separate programs for transferring files over a local network. The application uses Java Swing for the user interface and Java's built-in socket programming capabilities for network communication.

## Components

### 1. FileSender1
The sender application allows a user to select a file from their computer and send it to a receiver.

**Key features:**
- Simple GUI with a "Select File and Send" button
- File selection using Java's JFileChooser
- Acts as a server, waiting for a receiver to connect on port 1234
- Transfers file data in 4KB chunks for efficient memory usage

### 2. FileReceiver1
The receiver application connects to the sender and saves the received file to a specified location.

**Key features:**
- Simple GUI with a "Receive File" button
- Connects to localhost on port 1234
- Automatically creates the destination directory if it doesn't exist
- Saves received files to the configured destination path

## How It Works

1. **Setup**:
   - Launch both applications (FileSender1 and FileReceiver1) on the same machine
   - The sender acts as a server, the receiver acts as a client

2. **Sending process**:
   - User clicks "Select File and Send" on the sender application
   - User selects a file using the file chooser dialog
   - The application creates a ServerSocket on port 1234 and waits for a connection

3. **Receiving process**:
   - User clicks "Receive File" on the receiver application
   - The receiver establishes a connection to localhost:1234
   - Once connected, the sender reads the selected file and sends it over the socket
   - The receiver reads the incoming data and writes it to the specified location
   - Both applications display a success message upon completion

## Technical Details

- **Network protocol**: TCP/IP (using Java Sockets)
- **Buffer size**: 4096 bytes
- **Port**: Fixed to 1234
- **Connection type**: Localhost (127.0.0.1)

## Important Limitations and Considerations

1. **Default configuration needed**: You must configure the destination directory path before using the receiver application
2. **Fixed file name**: The receiver saves the file with a fixed name regardless of the original file type or name
3. **Connection handling**: The applications don't handle connection timeouts or retry mechanisms
4. **Single-threaded**: Both applications block during file transfer operations
5. **No progress indication**: Users don't see transfer progress during operation
6. **Sequential operation**: The sender must be ready before the receiver initiates the connection

## Setup Instructions

### Configuring File Paths
Before using the FileReceiver1 application, you need to set your desired destination directory:

1. Open the FileReceiver1.java file in a text editor
2. Locate the `receiveFile()` method
3. Change the `directoryPath` variable to your preferred directory:
   ```java
   String directoryPath = "YOUR_DESIRED_PATH_HERE";
   ```
4. Optionally modify the file name from "received_file.png" to something more appropriate for your use case:
   ```java
   String savePath = directoryPath + "\\YOUR_DESIRED_FILENAME";
   ```

## Potential Improvements

1. Add dynamic file naming based on the original file name
2. Implement a configurable destination directory via user interface
3. Add progress bars to show transfer status
4. Implement error handling and retry mechanisms
5. Support for transferring multiple files
6. Enable transfers across different machines (not just localhost)
7. Add encryption for secure file transfers
8. Implement file integrity verification
