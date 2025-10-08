package server;

public class SocketServer {

    // Port number the server listens on
    private final int PORT;

    // Main method to start the server
    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.setup();
        server.startAcceptingRequest();
    }

    // Default constructor that uses the default port number 5000
    public SocketServer() {
        this(5000);
    }

    // Parameterized constructor that sets a custom port number
    public SocketServer(int port) {
        this.PORT = port;
    }

    // Method to set up the server for connection (to be implemented later)
    public void setup() {
        // Empty for now - will initialize server socket later
    }

    // Method to start accepting client connections (to be implemented later)
    public void startAcceptingRequest() {
        // Empty for now - will handle socket connection logic later
    }

    // Getter for the PORT attribute
    public int getPort() {
        return PORT;
    }
}


