//Sam Dressler
//Gues Game Server

import java.io.*;
import java.net.*;

public class GuessGameServer {
    public static void main(String[] args) throws IOException {
       if (args.length != 2) {
            System.err.println("Usage: java GuessGameServer <port number> <maximum number for game>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        int maxGuessValue = Integer.parseInt(args[1]);
        try {
            //create a serverSocket using the port number provided by the user
            ServerSocket serverSocket = new ServerSocket(portNumber);
            
            //display which InetAddress the server is listening on and the local port
            System.out.println("The server is listening at: " + 
                    serverSocket.getInetAddress() + " on port " + 
                    serverSocket.getLocalPort());

            //create a client socket that is listening on the serer socket for a connection to be made
            //and then accepts it
            Socket clientSocket = serverSocket.accept();     
            //Create a PrintWriter that writes to the accepted sockets already existing output stream
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);                   

            //Create a buffered reader that reads the accepted sockets input stream
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Send number that the user entered to be the max value for the game
            output.println(maxGuessValue);

            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {   
                System.out.println("I received: " + inputLine);
                output.println(inputLine.toUpperCase());
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
