package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
     * Gson class used to do serialization
     */
    private final Gson gson;
    
    /**
     * Stores the client connection.
     */
    private final Socket socket;

    /**
     * Stores the client's username.
     */
    private final String currentUsername;

    /**
     * Input stream for receiving data from the client.
     */
    private DataInputStream dataInputStream;

    /**
     * Output stream for sending data to the client.
     */
    private DataOutputStream dataOutputStream;

    /**
     * Logger for server handler responses.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    /**
     * Default constructor that creates a ServerHandler instance.
     *
     * @param socket The socket representing the client connection.
     * @param username The username of the connected client.
     */
    public ServerHandler(Socket socket, String username) {
        this.socket = socket;
        this.currentUsername = username;
        this.gson = new GsonBuilder().serializeNulls().create();

        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Error can not establish input or output stream variables", e);
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
    }

    /**
     * Returns the socket associated with this connection.
     *
     * @return the Socket object for this connection
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Returns the current username associated with this connection.
     *
     * @return the current username as a String
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
}