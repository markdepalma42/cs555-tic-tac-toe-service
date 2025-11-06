package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socket.GamingResponse;
import socket.Request;
import socket.Response;
import socket.Response.ResponseStatus;

import java.io.*;
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
     * Gson class used to do serialization
     */
    private final Gson gson;

    /**
     * Static Event variable initialized with default values and move set to -1.
     * This represents the current event state, starting with default values indicating no move has been made.
     */
    public static Event event = new Event(0, null, null, null, null, -1);

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
     * @param socket   The socket representing the client connection.
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
     * Main request handler that processes client requests and returns appropriate responses.
     *
     * @param request the request object received from the client
     * @return a response object based on the request type
     */
    public Response handleRequest(Request request) {
        if (request == null) {
            LOGGER.warn("Received null request");
            return new Response(ResponseStatus.FAILURE, "Request cannot be null");
        }

        // Use switch-case to decide among the two request types
        switch (request.getType()) {
            case SEND_MOVE:
                return handleSendMoveRequest(request);
            case REQUEST_MOVE:
                return handleRequestMove();
            case LOGIN:
            case REGISTER:
            case UPDATE_PAIRING:
            case SEND_INVITATION:
            case ACCEPT_INVITATION:
            case DECLINE_INVITATION:
            case ACKNOWLEDGE_RESPONSE:
            case ABORT_GAME:
            case COMPLETE_GAME:
            default:
                // Return failed response if neither of the two types is sent
                LOGGER.warn("Unsupported request type: {}", request.getType());
                return new Response(ResponseStatus.FAILURE, "Unsupported request type: " + request.getType());
        }
    }

    /**
     * Handles SEND_MOVE requests by deserializing the move data from the request
     * and delegating to the handleSendMove(move) function for processing.
     *
     * @param request the SEND_MOVE request containing move data in the data attribute
     * @return a response indicating success or failure of move processing
     */
    private Response handleSendMoveRequest(Request request) {
        try {
            // Deserialize the data attribute to get the move
            int move = Integer.parseInt(request.getData());

            // Call the separate function with just the move parameter
            return handleSendMove(move);

        } catch (NumberFormatException e) {
            // Add logging for invalid move format
            LOGGER.warn("Invalid move format: {}", request.getData());
            return new Response(ResponseStatus.FAILURE, "Invalid move format: " + request.getData());
        } catch (Exception e) {
            // Log the exception with stack trace
            LOGGER.warn("Error processing move.", e);
            return new Response(ResponseStatus.FAILURE, "Error processing move: " + e.getMessage());
        }
    }

    /**
     * Processes the move by updating the game state and ensuring valid turn order.
     * This function sets the move and turn attributes of the static event variable
     * and returns a standard Response with SUCCESS status and appropriate message.
     *
     * @param move the integer representing the move to be processed
     * @return a response indicating success or failure of the move
     */
    private Response handleSendMove(int move) {
        // Get the current username
        String currentUser = getCurrentUsername();

        // Check to see if the last move was not made by the same user
        if (event.getTurn() != null && event.getTurn().equals(currentUser)) {
            return new Response(ResponseStatus.FAILURE, "Cannot make consecutive moves. Wait for opponent's move.");
        }

        // Set the move and turn attribute of the static variable event
        event.setMove(move);
        event.setTurn(currentUser);

        // Return a standard Response with SUCCESS status and appropriate message
        return new Response(ResponseStatus.SUCCESS, "Move " + move + " received successfully");
    }

    /**
     * Handles REQUEST_MOVE requests by retrieving the opponent's move from the static event.
     * Returns a GamingResponse with the move or -1 if no move is available, and deletes
     * the move after sending it to prevent duplicate processing.
     *
     * @return a GamingResponse containing the opponent's move and game status
     */
    private GamingResponse handleRequestMove() {
        // Get the move from the static variable event
        int move = event.getMove();
        String user = event.getTurn();
        GamingResponse response;

        // Check if there is a valid move made by the opponent, else set the move as -1
        if ((move == -1) || (user == null) || (user.equals(this.currentUsername))) {
            // No move available from opponent - create response with move = -1
            response = new GamingResponse(-1, true);
        } else {
            // Valid move available - create response with the actual move
            response = new GamingResponse(move, true);

            // Delete the move once it is sent to the opponent
            event.setMove(-1);
        }

        // Set the response status
        response.setStatus(ResponseStatus.SUCCESS);
        return response;
    }

    /**
     * The main execution method that runs in a separate thread to handle client communication.
     * This method processes incoming client requests, executes appropriate business logic,
     * and sends responses back to the client.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Read serialized request from client
                String serializedRequest = dataInputStream.readUTF();
                LOGGER.debug("Received request: {}", serializedRequest);

                // Deserialize request
                Request request = gson.fromJson(serializedRequest, Request.class);

                // Handle request and get response
                Response response = handleRequest(request);

                // Serialize and send response
                String serializedResponse = gson.toJson(response);
                dataOutputStream.writeUTF(serializedResponse);
                dataOutputStream.flush();
                LOGGER.debug("Sent response: {}", serializedResponse);

            } catch (EOFException e) {
                // Client disconnected
                LOGGER.info("Client disconnected.");
                break;
            } catch (IOException e) {
                LOGGER.error("I/O error: ", e);
                break;
            } catch (JsonSyntaxException e) {
                LOGGER.error("Invalid JSON format: ", e);
                // Optionally send error response to client
            } catch (Exception e) {
                LOGGER.error("Unexpected error: ", e);
            }
        }
        close();
    }

    /**
     * Closes the client connection and releases all associated resources.
     * This method ensures proper cleanup of sockets, streams, and other resources
     * when a client disconnects or when the server needs to terminate the connection.
     */
    public void close() {
        LOGGER.info("Attempting to close client connection for user: {}", currentUsername);
        quietClose(this.dataInputStream);
        quietClose(this.dataOutputStream);
        quietClose(this.socket);
        LOGGER.info("Handler shutdown complete for user: {}", currentUsername);
    }

    /**
     * Attempts to close any Closeable resource quietly.
     * Logs a warning if an IOException occurs but does not interrupt the shutdown sequence.
     *
     * @param closeable the resource to close
     */
    private void quietClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Error closing an instance of {}.", closeable.getClass().getSimpleName(), e);
        }
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
