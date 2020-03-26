/**
 * Client.java
 * Benjamin Stafford
 */
package client;

import api.Worker;
import api.ClientManager;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ClientBen {
    public static void main(String[] args){
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String userId, host, port;
        userId = host = port = "";
        if(args.length == 3){
            userId = args[0];
            host = args[1];
            port = args[2];
        } else if(args.length == 2){
            userId = args[0];
            host = args[1];
            port = "1099";
        } else if(args.length == 1){
            userId = args[0];
            host = "localhost";
            port = "1099";
        } else {
            System.err.println("Usage: javac Client <userId> <host> <port>");
            System.exit(0);
        }
        Scanner sc = new Scanner(System.in);
        String url = "rmi://" + host + ":" + port + "/" + ClientManager.REMOTE_OBJECT;
        try {
            ClientManager clientManager = (ClientManager) Naming.lookup(url);
            List<String> tasks = clientManager.register(userId);
            System.out.println("-------------------------");

            for(int i = 0; i < 5; i++){
                // Print all tasks
                System.out.println("All available tasks:");
                for(int j = 1; j <= tasks.size(); j++){
                    System.out.println("\t" + j + ": " + tasks.get(j - 1));
                }

                // Print menu
                System.out.println("Please select the task:");
                int taskIndex = sc.nextInt();
                while(taskIndex < 0 || taskIndex > tasks.size()){
                    System.out.println("Please enter a valid task number:");
                    taskIndex = sc.nextInt();
                }
                taskIndex--;
                System.out.println("You selected: " + tasks.get(taskIndex));

                Worker worker = clientManager.requestWork(userId, tasks.get(taskIndex));
                worker.doWork();

                System.out.println("Completed " + (i + 1) + " out of 5 tasks!");
                System.out.println("-------------------------");

                clientManager.submitResults(userId, worker);
            }
            System.out.println("Client score is: " + clientManager.getScore(userId));
        } catch(Exception e){
            e.printStackTrace();
        }
        sc.close();
    }
}