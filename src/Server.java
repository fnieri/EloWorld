import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Server class
class Server {
    public static void main(String[] args) {
        ServerSocket server = null;
        ArrayList<ClientHandler> connectedClients = new ArrayList<>();

        try {
            // server is listening on port 8080
            server = new ServerSocket(8080);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock = new ClientHandler(client, connectedClients);

                connectedClients.add(clientSock);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}