package server;

import model.Event;

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
     * Static Event variable initialized with default values and move set to -1.
     * This represents a default event indicating no move has been made.
     */
    public static Event defaultEvent = new Event(0, null, null, null, null, -1);

    /**
     * Default constructor that creates a ServerHandler instance.
     */
    public ServerHandler() {
        // Empty for now - will set up I/O streams later
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
