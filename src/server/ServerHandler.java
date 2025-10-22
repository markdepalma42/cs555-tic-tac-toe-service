package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles I/O communication between the server and a single client connection.
 * This class extends Thread to enable concurrent handling of multiple client connections.
 * <p>
 * Each ServerHandler instance manages all communication with one connected client,
 * processing various request types and maintaining the client session state. The handler
 * runs in its own thread to allow the main server to continue accepting new connections
 * while serving existing clients.
 * <p>
 * This class is responsible for processing all request types from clients, including
 * login, registration, game invitations, and gameplay moves.
 */
public class ServerHandler extends Thread {
    /**
     * Input stream for receiving data from the client
     */
    private DataInputStream dataInputStream;

    /**
     * Output stream for sending data to the client.
     */
    private DataOutputStream dataOutputStream;

    /**
     * The socket connection to the client.
     */
    private Socket clientSocket;

    /**
     * Default constructor that creates a ServerHandler instance.
     */
    public ServerHandler() {
        try {
            this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e)
        {
            System.err.println("Error can not establish input or output stream variables");
        }
    }

    /**
     * The main execution method that runs in a separate thread to handle client communication.
     * This method processes incoming client requests, executes appropriate business logic,
     * and sends responses back to the client.
     */
    @Override
    public void run() {
        // Empty for now - will process client requests later
    }

    /**
     * Closes the client connection and releases all associated resources.
     * This method ensures proper cleanup of sockets, streams, and other resources
     * when a client disconnects or when the server needs to terminate the connection.
     */
    public void close() {
        // Empty for now - will close sockets and streams later
    }
}
