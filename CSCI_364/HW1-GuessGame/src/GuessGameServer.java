//Sam Dressler
//Gues Game Server

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class GuessGameServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    
    public void setup(int port){
        try{
            //create a serverSocket using the port number provided by the user
            serverSocket = new ServerSocket(port);
            //display which InetAddress the server is listening on and the local port
            System.out.println("The server is listening at: " + serverSocket.getInetAddress() + " on port " + 
                serverSocket.getLocalPort());
             System.out.println("Waiting on Client Side..."); 
            //create a client socket that is listening on the serer socket for a connection to be made
            //and then accepts it
            clientSocket = serverSocket.accept();   
            //Create a PrintWriter that writes to the accepted sockets already existing output stream
            output = new PrintWriter(clientSocket.getOutputStream(), true);  
            //Create a buffered reader that reads the accepted sockets input stream
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Game Started...");
        }
        catch(IOException e){
            System.out.println("<Unable to complete Setup>");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void playGame(int value){
         //Send number that the user entered to be the max value for the game
        output.println(value);
        int randomNumber =0;
        randomNumber = (int)(Math.random() * value);
       // System.out.println("Random Number = " + randomNumber);
        int inputValue =0;
        int guessCounter = 0;
        String temp;
        try{
            while ((temp = input.readLine()) != null) {   
                inputValue = convertGuessToInt(temp);
                 if(inputValue > randomNumber) {
                    System.out.println("User Guessed: " + inputValue + " -- Too High!");
                    output.println("Too High! Guess Lower");
                    guessCounter++;
                }
                else if (inputValue < randomNumber) {
                    System.out.println("User Guessed: " + inputValue + " -- Too Low!");
                    output.println("Too Low! Guess Higher");
                    guessCounter++;
                }
                else {
                guessCounter++;
                    System.out.println("User Guessed the number ("+randomNumber+
                    ") Correctly after ("+guessCounter+") attempts!");
                    output.println("Success! you have guessed the number in "+guessCounter+" attempts!");
                    clientSocket.close();
                    serverSocket.close();
                }
            }
        }
        catch(IOException e){
            System.out.println("<User client has been closed>");
        }
    }
    public boolean getRepeatedPlay(){
        boolean playAgain = false;
        System.out.println("Game Completed, Would you like to play again?\n<Y/N>");
        Scanner sc = new Scanner(System.in);
        String response = sc.next();
        if(response.equalsIgnoreCase("Y")){
            playAgain = true;
        }
        else{
            sc.close();
            playAgain = false;
        }
        return playAgain;
    }
    public static void main(String[] args) throws IOException {
        int port = 0;
        int maxGuessValue = 0;
        boolean repeatedPlay = false;
        //check to see if the server was started with the correct amount of arguments
        if (args.length != 2) {
            System.err.println("Usage: java GuessGameServer <port number> <maximum number for game>");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        maxGuessValue = Integer.parseInt(args[1]);
        //create an instance of the GuessGameServer
        GuessGameServer server = new GuessGameServer();
        //Run the socket setup in a seperate function
        server.setup(port);
        do{
            server.playGame(maxGuessValue);
        
            repeatedPlay = server.getRepeatedPlay();

            if(repeatedPlay == true){
                server.setup(port);
             }
            else{
                System.out.println("Thanks for playing!");
                System.exit(0);
            }
        } while(true);  
    }
    private static int convertGuessToInt(String guess) throws NumberFormatException{
        return Integer.parseInt(guess);
    }
}
