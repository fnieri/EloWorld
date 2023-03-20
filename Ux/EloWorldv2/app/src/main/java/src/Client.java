package src;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;

import src.Enum.AuthActions;
import src.Enum.Domain;

// Client class
public class Client {

    PrintWriter out;
    BufferedReader in;
    Socket socket;
    Model model = new Model();
    Controller controller = new Controller(model, this);

    public Client() throws IOException {
    }

    // driver code
    public void main() {
        System.out.println("I have been launched");
        // establish a connection by providing host and port 8080
        try {
            socket = new Socket("10.0.30.11", 8080);

            // writing to server
            out = new PrintWriter(socket.getOutputStream(), true);

            // reading from server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String packet;

            while (null != (packet = in.readLine())) {
                controller.parseRequest(packet);
            }

            // closing the scanner object
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(JSONObject message) {
        try {
            String messageString = message.toString();
            out.println(messageString);
            out.flush();
            // Reset flag
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Model getModel() {
        return controller.getModel();
    }

    public Controller getController() {
        return controller;
    }

}