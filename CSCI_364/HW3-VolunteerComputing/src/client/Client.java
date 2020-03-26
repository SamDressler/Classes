//Sam Dressler
//HW 3 Client
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;

import api.ClientManager;
import api.Worker;

public class Client
{
    //Variables
    private static String user_id;
    private static int rmi_port = 1099;
    private static String rmi_host = "localhost";
    private static final int tasks_required = 5;
    private static int tasks_completed = 0;
    private static List<String> tasks = new LinkedList<String>();
    private static HashMap <String,Integer> users = new HashMap<>();
    public static void main(final String args[]) 
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        //Variables
        Scanner sc = new Scanner(System.in);
        int mode = 0;
        String taskID = null;
        
        //Check the user arguments
        read_args(args);
    
        System.out.println("Client Started:");
        try {

            final String url = "rmi://" + rmi_host + ClientManager.REMOTE_OBJECT;

            ClientManager cm = (ClientManager)Naming.lookup(url);
            //Register once
            tasks = cm.register(user_id);
            //Print Possible Tasks
            System.out.print("Possible Tasks: ");
            for(int i = 0; i <tasks.size(); i ++){
                System.out.print(tasks.get(i)+", ");
            }
            //NOTE: This was not part of the requirements but was added to make testing easier

            //Request and do work 5 times
            System.out.println("\nComplete 5 tasks...");            
            mode = programMode(sc);
            switch(mode){
                case 1:
                    //I think this is the mode that the assignment wants
                    //Randomly choosing and executing 5 tasks
                    int task = 0;
                    Random rand = new Random();
                    task = rand.nextInt(4);
                    while(tasks_completed < tasks_required)
                    {

                    }
                    break;

                case 2:
                    //the mode I added (╯°□°）╯︵ ┻━┻
                    String taskName = null;
                    String uid = getUserID();
                    while(tasks_completed < tasks_required)
                    {
                        taskName = chooseWork(sc);
                        try{
                            Worker worker = cm.requestWork(uid, taskName);
                            worker.doWork();
                            cm.submitResults(uid, worker);
            
                        }
                        catch(RemoteException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    System.out.println("Error: Invalid Mode");
                    System.exit(0);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        //users = ro.getUsers();
       // System.out.println("Final Score"+ users.get(user_id));
        System.exit(0);
    }

    /**
     * Program Mode : Function allows choice of execution type
     * Ramdomly choose 5 tasks
     * OR 
     * User Selects 5 tasks
     * @param Scanner
    */
    private static int programMode(Scanner sc)
    {
        int choice = 0;
        int mode = 0;
        boolean bool = true;
        sc = new Scanner(System.in);
        System.out.println("Choose the Execution Type:");
        System.out.println("1. Randomly choose tasks");
        System.out.println("2. Select Each task\nChoice: ");
        choice = sc.nextInt();
        while(bool)
        {
            switch(choice)
            {
                case 1:
                    mode = 1; //Randomly show execute each task
                    bool = false;
                    break;
                case 2:
                    mode = 2; //Choose what task is done each time
                    bool = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
                    bool = true;
                    break;
           }
        }
        sc.close();
        return mode;
    }
    
    /**
    * chooseWork() 
    * @return String Task ID
    */
    private static String chooseWork(Scanner sc)
    {
        String task = null;
        int choice = 0;
        sc = new Scanner(System.in);
        System.out.println("Choose a task to complete:");
        System.out.println("1. Sort a sequence of values");
        System.out.println("2. Sum a sequence of numerical values(int, float, or double)");
        System.out.println("3. Test if an integer is prime");
        System.out.println("4. Reduce a fraction\nChoice: ");
        choice = sc.nextInt();
        switch(choice)
        {
            case 1:
                task = "SortWorker";
                break;
            case 2:
                task = "SumReducer";
                break;
            case 3:
                task = "PrimeChecker";
                break;
            case 4:
                task = "FractionReducer";
                break;
            default:
                System.out.println("Invalid Choice");
                task = "bad";
        }
        sc.close();
        return task;
    }
    /*
     * read_args takes the users input and fills the useful variables
     */
    private static void read_args(final String args[]) 
    {
        final int num_args = args.length;
        switch(num_args)
        {
            //just client userid is specified
            case 1:
                user_id = args[0];
            break;

            //remi host
            case 2:
                user_id = args[0];
                rmi_host = args[1];
            break;
            //rmi port
            
            case 3:
                user_id = args[0];
                rmi_host = args[1];
                rmi_port = Integer.parseInt(args[2]);
            break;
            
            //all default values are used
            default:
                System.out.println("ERROR: Must atleast provide the user id");
                System.exit(0);
        }
    }
    private static String getUserID(){
        return user_id;
    }
}
