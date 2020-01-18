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
            //create the print writer that sends the guess values to the server
            PrintWriter output = new PrintWriter(echoSocket.getOutputStream(), true);
            //create buffered reader for response from the server
            BufferedReader input = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            //create buffered reader for user input that is sent to server
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            String temp;
            if ((userInput = input.readLine()) != null){
                System.out.println("Game starting...");
                System.out.println("Server: Guess a number in the range <0 - "+ userInput+">");
            }
            while ((userInput = stdIn.readLine()) != null) {
                //send the server the user's guess
                output.println(userInput);
                //get server's response 
                temp = input.readLine();
               //output the server's response from the servers printwriter
                System.out.println("Server: " + temp);
                if(temp.contains("Success!")){
                    System.exit(0);
                }
            }

            echoSocket.close();
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
