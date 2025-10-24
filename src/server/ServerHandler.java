package server;

import model.Event;
import socket.Request;
import socket.Response;
import socket.GamingResponse;
import socket.Response.ResponseStatus;

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
     * Main request handler that processes client requests and returns appropriate responses.
     *
     * @param request the request object received from the client
     * @return a response object based on the request type
     */
    public Response handleRequest(Request request) {
        if (request == null) {
            return new Response(ResponseStatus.FAILURE, "Request cannot be null");
        }

        // Use switch-case to decide among the two request types
        switch (request.getType()) {
            case SEND_MOVE:
                return handleSendMoveRequest(request);
            case REQUEST_MOVE:
                return handleRequestMove();
            default:
                // Return failed response if neither of the two types is sent
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
            return new Response(ResponseStatus.FAILURE, "Invalid move format: " + request.getData());
        } catch (Exception e) {
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
        if (defaultEvent.getTurn() != null && defaultEvent.getTurn().equals(currentUser)) {
            return new Response(ResponseStatus.FAILURE, "Cannot make consecutive moves. Wait for opponent's move.");
        }

        // Set the move and turn attribute of the static variable event
        defaultEvent.setMove(move);
        defaultEvent.setTurn(currentUser);

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
        try {
            // Get the move from the static variable event
            int move = defaultEvent.getMove();
            GamingResponse response;

            // Check if there is a valid move made by the opponent, else set the move as -1
            if (move == -1) {
                // No move available from opponent - create response with move = -1
                response = new GamingResponse(-1, true);
            } else {
                // Valid move available - create response with the actual move
                response = new GamingResponse(move, true);
            }

            // Delete the move once it is sent to the opponent
            defaultEvent.setMove(-1);
            return response;

        } catch (Exception e) {
            // Return a failure response with move=-1 and active=false on error
            return new GamingResponse(-1, false);
        }
    }

    /**
     * Gets the current username for the client connected to this ServerHandler.
     * This needs to be implemented based on how you track user sessions.
     *
     * @return the username of the current client
     */
    private String getCurrentUsername() {
        // TODO: Implement this method based on your authentication/session management
        // For now, return a placeholder - you'll need to replace this with actual implementation
        // You might store the username when the user logs in, or get it from the connection context
        return "current_user"; // Placeholder - implement properly
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
