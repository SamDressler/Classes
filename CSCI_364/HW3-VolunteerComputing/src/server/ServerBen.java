/**
 * Server.java
 * Benjamin Stafford
 */
package server;

import api.ClientManager;

import java.rmi.Naming;

public class ServerBen {
    public static void main(String[] args){
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String host, port;
        host = port = "";
        if(args.length == 2){
            host = args[0];
            port = args[1];
        } else if(args.length == 1){
            host = args[0];
            port = "1099";
        } else if(args.length == 0){
            host = "localhost";
            port = "1099";
        } else {
            System.err.println("Usage: javac Server <host> <port>");
            System.exit(0);
        }
        String url = "rmi://" + host + ":" + port + "/" + ClientManager.REMOTE_OBJECT;
        try {
            Naming.rebind(url, new RemoteObject());
            System.out.println("Server started on port " + port);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}