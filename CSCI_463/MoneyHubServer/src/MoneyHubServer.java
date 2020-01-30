//Sam Dressler
//Gues Game Server
//CSCI 364
//1-18-2020

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
//import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Scanner;
public class MoneyHubServer {
    static ServerSocket server;
    static Socket client;
    BufferedReader input;
    PrintWriter output;
    static InputStream is;
    static OutputStream os;

    public static void main(final String[] args) {
        int port = 0;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            System.out.println("Usage: java MoneyHubServer <port>");
            System.exit(0);
        }
        System.out.println("Welcome!");
        try {
            setup(port);
            processMessage();
            client.close();
            server.close();
            is.close();
        } catch (final IOException e) {
            System.out.println(">>Setup Failed");
            e.printStackTrace();
        }


    }

    public static void setup(final int port) throws IOException {
        server = new ServerSocket(port);
        client = server.accept();

        System.out.println("The server is listening at: " + server.getInetAddress() + " on port " + server.getLocalPort());

        is = client.getInputStream();
        os = client.getOutputStream();

    }
    public static String processMessage(){
        try{
            //get the length of the message being sent and then
            //the bytes of the message
            byte[] lenBytes = new byte[4];
            is.read(lenBytes, 0, 4);
            int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                      ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
            byte[] receivedBytes = new byte[len];
            is.read(receivedBytes, 0, len);
            String msgReceived = new String(receivedBytes, 0, len);
    
            System.out.println("Server received: " + msgReceived);
    
            // Sending message to the client
            String messageToSend = msgReceived;
            byte[] toSendBytes = messageToSend.getBytes();
            int toSendLen = toSendBytes.length;
            byte[] toSendLenBytes = new byte[4];
            toSendLenBytes[0] = (byte)(toSendLen & 0xff);
            toSendLenBytes[1] = (byte)((toSendLen >> 8) & 0xff);
            toSendLenBytes[2] = (byte)((toSendLen >> 16) & 0xff);
            toSendLenBytes[3] = (byte)((toSendLen >> 24) & 0xff);
            os.write(toSendLenBytes);
            os.write(toSendBytes);
        }
        catch(final IOException e) {
            System.out.println(">>Setup Failed");
            e.printStackTrace();
        }

        return "Yes";
    }
}