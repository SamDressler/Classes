//Sam Dressler
//Client.java
package client;

import java.rmi.RemoteException;
import java.rmi.Naming;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.Scanner;

import api.ClientManager;
import api.Worker;
/**
 * @author Sam Dressler
 */
public class Client
{
    //Variables
    private static String user_id;
    private static int rmi_port = 1099;
    private static String rmi_host = "localhost";
    private static final int tasks_required = 5;
    private static int tasks_completed = 0;
    private static List<String> tasks = new LinkedList<String>();
    public static void main(final String args[]) 
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        //Variables
        Scanner sc = new Scanner(System.in);
        int mode = 0;
        String taskName = null;
        
        //Check the user arguments
        read_args(args);
    
        final String url = "rmi://" + rmi_host +":"+ rmi_port +"/"+ ClientManager.REMOTE_OBJECT;
        try {
            ClientManager cm = (ClientManager)Naming.lookup(url);
            //Register once
            tasks = cm.register(user_id);

            System.out.println("Client Started:");
            //Print Possible Tasks 
            System.out.println("=========================");
            System.out.println("Possible Tasks: ");
            for(int i = 0; i <tasks.size(); i ++){
                System.out.print(tasks.get(i)+"\n");
            }
            System.out.println("=========================");

            //NOTE: This was not part of the requirements but was added to make testing easier

            //Request and do work 5 times          
            mode = programMode(sc);
            switch(mode){
               //Randomly Choose and execute 5 tasks
                case 1:
                    System.out.println("Randomly choosing and executing tasks: ");
                    System.out.println("-------------------------------------");
                    String uid = getUserID();
                    Random rand = new Random();
                    
                    while(tasks_completed < tasks_required)
                    {
                        System.out.println("----------------------------------");
                        int rand_task = rand.nextInt(4);
                        taskName = tasks.get(rand_task);
                        try
                        {
                            Worker worker = cm.requestWork(uid, taskName);
                            System.out.println("---Task ID: "+(worker.getTaskId()+1));
                            System.out.println("---Task being completed: "+ worker.getTaskName());
                            worker.doWork();
                            cm.submitResults(uid, worker);
                            System.out.println("---"+user_id+"'s Current Total Score: "+ cm.getScore(user_id));
                            System.out.println("----------------------------------");
                            tasks_completed++;
                        }
                        catch(RemoteException e)
                        {
                            e.printStackTrace();
                            break;
                        }

                    }
                    break;

                case 2:
                    //the mode I added (╯°□°）╯︵ ┻━┻
                    uid = getUserID();
                    while(tasks_completed < tasks_required)
                    {
                        System.out.println("=============================");
                        System.out.println("Tasks Remaining In Session: "+(tasks_required-tasks_completed));
                        System.out.println("=============================");
                        taskName = chooseWork(sc);
                        try{
                            Worker worker = cm.requestWork(uid, taskName);
                            System.out.println("---Task ID: "+(worker.getTaskId()+1));
                            System.out.println("---Task being completed: "+ worker.getTaskName());
                            worker.doWork();
                            cm.submitResults(uid, worker);
                            tasks_completed++;
                        }
                        catch(RemoteException e){
                            e.printStackTrace();
                            break;
                        }
                        System.out.println("---"+user_id+"'s Current Total Score: "+ cm.getScore(user_id));
                        System.out.println("-------------------------------------");
                    }
                    System.out.println("----------------------------------");
                    break;
                default:
                    System.out.println("Error: Invalid Mode");
                    System.exit(0);
            }
        }
         catch (final Exception e) 
         {
            e.getMessage();
            e.printStackTrace();
        }

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

        while(bool)
        {        
            System.out.println("Choose Method to Complete 5 tasks");
            System.out.println("1. Randomly choose tasks");
            System.out.println("2. Explicitly Select Each task\nChoice: ");
            choice = sc.nextInt();
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
        boolean bool = true;
        while(bool)
        {
            System.out.println("Choose a task to complete:");
            System.out.println("1. Sort a sequence of values");
            System.out.println("2. Sum a sequence of numerical values");
            System.out.println("3. Test if an integer is prime");
            System.out.println("4. Reduce a fraction\nChoice: ");
            choice = sc.nextInt();
            switch(choice)
            {
                case 1:
                    task = "SortWorker";
                    bool = false;
                    break;
                case 2:
                    task = "SumReducer";
                    bool = false;
                    break;
                case 3:
                    task = "PrimeChecker";
                    bool = false;
                    break;
                case 4:
                    task = "FractionReducer";
                    bool = false;
                    break;
                default:
                    System.out.println("Invalid Choice");
                    bool = true;
                    break;
           }
        }
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
