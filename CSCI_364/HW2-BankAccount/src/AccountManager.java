//Sam Dressler 
//CSCI 364
//Program Usage is java AccountManager 2 4 500 3000
//Your program shall read command line arguments that indicate the number of deposit threads,
//the number of withdrawal threads, the initial account balance (an integer), and the number of
//milliseconds the main thread must sleep after starting the other threads and before interrupting
//the other threads. 
public class AccountManager
{
    public static void main(String[] args)
    {
        if(args.length != 4)
        {
            System.out.println("Error: Invalid Number of Argument");    
            System.exit(0);
        }

        if((Integer.parseInt(args[0]) < 1) || (Integer.parseInt(args[1]) < 1)){
            System.out.println("Error: Program requires at least 1 Deposit and Withdrawl Thread");
            System.exit(0);    
        }

        if(Integer.parseInt(args[2]) < 0)
        {
            System.out.println("Error: Account Balance cannot be below zero");            
            System.exit(0);
        }

        int numDThreads = Integer.parseInt(args[0]);
        int numWThreads = Integer.parseInt(args[1]);
        int balance     = Integer.parseInt(args[2]);
        final int maxBalance = balance * 2;
        int waitTime    = Integer.parseInt(args[3]);
        //get singleton from the private constructor;
        Account account = Account.getInstance();
        account.setBalance(balance);
        
        //initialize the deposit threads
        for(int i = 0; i <numDThreads; i++)
        {
                        
        }
             
   
    }
}
