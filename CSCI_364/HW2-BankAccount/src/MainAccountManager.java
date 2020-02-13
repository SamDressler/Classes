//Sam Dressler 
//CSCI 364
//Program Usage is java AccountManager 2 4 500 3000
//Your program shall read command line arguments that indicate the number of deposit threads,
//the number of withdrawal threads, the initial account balance (an integer), and the number of
//milliseconds the main thread must sleep after starting the other threads and before interrupting
//the other threads. 
public class MainAccountManager
{
    public static void main(final String[] args) {
        if (args.length != 4) {
            System.out.println("Error: Invalid Number of Argument");
            System.exit(0);
        }

        if ((Integer.parseInt(args[0]) < 1) || (Integer.parseInt(args[1]) < 1)) {
            System.out.println("Error: Program requires at least 1 Deposit and Withdrawl Thread");
            System.exit(0);
        }

        if (Integer.parseInt(args[2]) < 0) {
            System.out.println("Error: Account Balance cannot be below zero");
            System.exit(0);
        }

        final int numDThreads = Integer.parseInt(args[0]);
        final int numWThreads = Integer.parseInt(args[1]);
        final int balance = Integer.parseInt(args[2]);
        final int maxBalance = balance * 2;
        final int waitTime = Integer.parseInt(args[3]);

        //Create thread array to store the threads
        int totalThreads = (numDThreads + numWThreads);
        final Thread threadList [totalThreads];
        threadCounter = 0;
        // get singleton from the private constructor;
        final Account account = Account.getInstance();
        account.setBalance(balance);
        System.out.println("Starting Balance: " + account.getBalance());
        System.out.println("---------------------------");
        String threadName;
        //initialize the deposit threads
        for (int i = 0; i < numDThreads; i++) 
        {
            threadName = "Depsoitor_" + i;
            //Create a DepositThread object named dr (deposit runnable)
            final DepositThread dr = new DepositThread(threadName, maxBalance, account);
            //create a new deposit thread or dt from the dr runnable object
            Thread dt = new Thread(dr);
            //store the created thread in the array
            threadList[threadCounter] = dt;
            //increment threadcounter
            threadCounter++;
            dt.start();
            System.out.println("Depositor " + i + " created");
        }

        // Initialize the withdrawl threads
          for (int i = 0; i < numWThreads; i++) 
        {
            threadName = "Withdrawer_" + i;
            //create a WithdrawlThread object named wr (ithdrawl runnable)
            final WithdrawlThread wr = new WithdrawlThread(threadName, account);
            //create a new withdrawl thread or wt from the wr runnable object
            Thread wt = new Thread(wr);
            //store the created thread in the thread array
            threadList[threadCounter] = wt;
            threadCounter++;
            wt.start();
            System.out.println("Withdrawer " + i + " created");
         }
        // Sleep the main thread for the amount of time set from the command line
        final Thread t = Thread.currentThread();

        t.setName("Main Thread");
        System.out.println(t.getName() + " is sleeping"); 
        try 
        {
            Thread.sleep(waitTime);
            
        } 
        catch (InterruptedException e) {
            System.out.println("Main Thread Interupted");
            e.printStackTrace();
        }
        for(threadCounter=0; threadCounter < totalThreads; threadCounter++)
        {

        }
        System.out.println("Final Balance: " + account.getBalance());
    }
}
