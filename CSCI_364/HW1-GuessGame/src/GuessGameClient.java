//Sam Dressler
//Guess Game Client

import java.io.*;
import java.net.*;

public class GuessGameClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println("Usage: java GuessGameClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket echoSocket = new Socket(hostName, portNumber);

            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            if ((userInput = stdIn.readLine()) != null){
                System.out.println("Guess a number in the range 0 - "+ userInput);
            }
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Server: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
}
