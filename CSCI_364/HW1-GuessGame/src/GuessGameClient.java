//Sam Dressler
//CSCI 463
//1-28-2020

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class GuessGameClient {
    public static void main(final String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: java GuessGameClient <host name> <port number>");
            System.exit(1);
        }

        final String hostName = args[0];
        final int portNumber = Integer.parseInt(args[1]);

        try {
            final Socket echoSocket = new Socket(hostName, portNumber);
            // create the print writer that sends the guess values to the server
            final PrintWriter output = new PrintWriter(echoSocket.getOutputStream(), true);
            // create buffered reader for response from the server
            final BufferedReader input = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            // create buffered reader for user input that is sent to server
            final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            String temp;
            if ((userInput = input.readLine()) != null) {
                System.out.println("Welcome!\n");
                System.out.println("Server: Guess a number in the range <0 - " + userInput + ">");
            }
            while ((userInput = stdIn.readLine()) != null) {
                // send the server the user's guess
                output.println(userInput);
                // get server's response
                temp = input.readLine();
                // output the server's response from the servers printwriter
                System.out.println("Server: " + temp);
                if (temp.contains("Success!")) {
                    System.exit(0);
                }
            }

            echoSocket.close();
        } catch (final UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (final IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +hostName);
            System.exit(1);
        } 
    }
}
