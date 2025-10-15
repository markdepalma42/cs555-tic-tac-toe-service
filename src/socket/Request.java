package socket;

/**
 * Models all client requests sent to the server in the TicTacToe game system.
 * This class serves as the standard communication format between client and server,
 * encapsulating the request type and associated data for all game operations.
 * <p>
 * Clients must create Request objects to communicate with the server, and the server
 * always expects Request objects when receiving client communications. The request
 * type determines the operation to be performed, while the data field contains
 * serialized objects needed for the specific operation.
 */
public class Request {

    /**
     * The type of client request, determining which operation the server should perform.
     * Each request type corresponds to specific game functionality and determines how
     * the data field should be interpreted.
     */
    private RequestType type;

    /**
     * A string representation of serialized data sent by the client. The content and format
     * depend on the request type. Can contain serialized objects of String, Integer, or User classes.
     * For some request types, this field may be null when no additional data is required.
     */
    private String data;

    /**
     * Default constructor that creates a Request with null type and data.
     * Used for initialization before setting specific request parameters.
     */
    public Request() {
        this.type = null;
        this.data = null;
    }

    /**
     * Parameterized constructor that creates a Request with specific type and data.
     *
     * @param type the type of request being made
     * @param data the serialized data associated with the request, or null if not needed
     */
    public Request(RequestType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Returns the type of this request.
     *
     * @return the RequestType indicating what operation this request represents
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Returns the serialized data associated with this request.
     *
     * @return the serialized data string, or null if no data is associated
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the type of this request.
     *
     * @param type the RequestType to set for this request
     */
    public void setType(RequestType type) {
        this.type = type;
    }

    /**
     * Sets the serialized data for this request.
     *
     * @param data the serialized data string to associate with this request
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Enumeration defining all possible request types that clients can send to the server.
     * Each request type corresponds to specific game functionality and has defined data requirements.
     */
    public enum RequestType {
        /**
         * Sent when a user wants to log into the game. Data contains serialized User object with username and password.
         * Server responds with standard Response indicating success/failure.
         */
        LOGIN,

        /**
         * Sent when a user wants to register for the first time. Data contains serialized User object with username, display name, and password.
         * Server responds with standard Response indicating success/failure.
         */
        REGISTER,

        /**
         * Sent periodically after login to request pairing updates (available players, invitations, responses). Data is null.
         * Server responds with PairingResponse containing all pairing updates.
         */
        UPDATE_PAIRING,

        /**
         * Sent when a player selects an opponent to play a game. Data contains serialized String with opponent's username.
         * Server creates Event with status PENDING and responds with standard Response.
         */
        SEND_INVITATION,

        /**
         * Sent when a player accepts a game invitation. Data contains serialized Integer with eventId of the invitation.
         * Server updates Event status from PENDING to ACCEPTED and responds with standard Response.
         */
        ACCEPT_INVITATION,

        /**
         * Sent when a player declines a game invitation. Data contains serialized Integer with eventId of the invitation.
         * Server updates Event status from PENDING to DECLINED and responds with standard Response.
         */
        DECLINE_INVITATION,

        /**
         * Sent as acknowledgment after receiving a game response to an invitation. Data contains serialized Integer with eventId.
         * Server updates Event status from ACCEPTED/DECLINED to PLAYING/ABORTED and responds with standard Response.
         */
        ACKNOWLEDGE_RESPONSE,

        /**
         * Sent periodically during gameplay to request the opponent's move. Data is null.
         * Server responds with GamingResponse containing opponent's move and game active status.
         */
        REQUEST_MOVE,

        /**
         * Sent during gameplay to submit a player's move. Data contains serialized Integer (0-8) representing the grid cell selected.
         * Server responds with standard Response indicating success/failure.
         */
        SEND_MOVE,

        /**
         * Sent when a user wants to abort an ongoing game. Data is null.
         * Server responds with standard Response indicating success/failure.
         */
        ABORT_GAME,

        /**
         * Sent when a game is over after receiving a final move. Data is null.
         * Server updates Event status from PLAYING to COMPLETED and responds with standard Response.
         */
        COMPLETE_GAME
    }
}
