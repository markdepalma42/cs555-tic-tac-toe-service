package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * The main socket server controller class for the TicTacToe game server.
 * This class sets up the server socket, listens for incoming client connections,
 * and creates dedicated ServerHandler threads to manage each client session.
 * <p>
 * As the primary entry point for the TicTacToe server, this class handles the
 * initialization of the server environment and coordinates the acceptance of
 * multiple client connections concurrently. Each connected client is assigned
 * to a separate ServerHandler thread for independent request processing.
 * <p>
 * The server listens on a configurable port (default 5000) and maintains
 * continuous availability for client connections until shutdown.
 */
public class SocketServer {

    /**
     * The object of ServerSocket class for creating the socket server.
     */
    private ServerSocket serverSocket;

    //Logger instance
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    /**
     * The port number that the socket server listens on for incoming client connections.
     * This value is set during construction and remains constant for the server instance.
     */
    private final int PORT;

    /**
     * The main entry point that launches the TicTacToe server application.
     * Creates a SocketServer instance, performs setup initialization, and begins
     * accepting client connections.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.setup();
        server.startAcceptingRequest();
    }

    /**
     * Default constructor that initializes the server with the default port number 5000.
     * Delegates to the parameterized constructor to set the constant PORT value.
     */
    public SocketServer() {

        this(5000);
    }

    /**
     * Parameterized constructor that initializes the server with a custom port number.
     * Allows configuration of the specific port the server will listen on for connections.
     *
     * @param port the custom port number for the server to listen on
     * @throws IllegalArgumentException if the port is less than 0
     */
    public SocketServer(int port) {
        if (port < 0) {
            throw new IllegalArgumentException("Port number cannot be negative");
        }

        this.PORT = port;
    }

    /**
     * Performs initial server setup and configuration. Initializes the server socket,
     * configures connection parameters, and prepares the server environment for accepting
     * clients.
     */
    public void setup() {

        try {
            serverSocket = new ServerSocket(this.PORT);
            InetAddress localHost = InetAddress.getLocalHost();

            // Log server information
            LOGGER.info("Server started on port {}", this.PORT);
            LOGGER.info("Hostname: {}", localHost.getHostName());
            LOGGER.info("Host Address: {}", localHost.getHostAddress());
            LOGGER.info("Port Number: {}", serverSocket.getLocalPort());

        } catch (BindException e) {
            LOGGER.error("Port {} is already in use. Please choose another port.", this.PORT, e);
        } catch (SocketException e) {
            LOGGER.error("Socket error occurred: ", e);
        } catch (IOException e) {
            LOGGER.error("I/O error while opening the socket: ", e);
        }
    }

    /**
     * Starts the main server loop to accept incoming client connections.
     * This method runs continuously, accepting new client connections and
     * spawning ServerHandler threads for each connected client.
     */
    public void startAcceptingRequest() {
        // Empty for now - will handle socket connection logic later
    }

    /**
     * Returns the port number that the server is configured to listen on.
     *
     * @return the server's listening port number
     */
    public int getPort() {
        return PORT;
    }
}
