import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


// Server class
class Server {
    public static void main(String[] args) throws JSONException {
        ServerSocket server = null;
        ArrayList<ClientHandler> connectedClients = new ArrayList<>();
        ArrayList<JSONObject> receivedBlockChains = new ArrayList<>();
        JSONObject leaderBoard = new JSONObject();
        leaderBoard.put("Magnus Carlsen", 2852);

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
                ClientHandler clientSock = new ClientHandler(client, connectedClients, receivedBlockChains, leaderBoard);

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