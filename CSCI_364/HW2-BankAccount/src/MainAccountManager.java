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
        Thread[] myThreads = new Thread[totalThreads];
        //Create an array of the deposit and withdrawl objects so we can track the information
        DepositThread[] depositObjects = new DepositThread[numDThreads];
        WithdrawlThread[] withdrawObjects = new WithdrawlThread[numWThreads];
        

        int threadCounter = 0;
        // get singleton from the private constructor;
        
        final Account account = Account.getInstance();
        account.setBalance(balance);
        System.out.println("Starting Balance: " + account.getBalance());
        System.out.println("--------------------------------");
        
        String threadName;
        //initialize the deposit threads
        for (int i = 0; i < numDThreads; i++) 
        {
            threadName = "Depsoitor_" + i+1;
            //Create a DepositThread object named dr (deposit runnable)
            depositObjects[i] = new DepositThread(threadName, maxBalance, account);
            final DepositThread dr = depositObjects[i];
            //create a new deposit thread or dt from the dr runnable object
            Thread dt = new Thread(dr);
            //store the created thread in the array
            myThreads[threadCounter] = dt;
            //increment threadcounter
            threadCounter++;
            dt.start();
        }

        // Initialize the withdrawl threads
          for (int i = 0; i < numWThreads; i++) 
        {
            threadName = "Withdrawer_" + i+1;
            //create a WithdrawlThread object named wr (ithdrawl runnable)
            withdrawObjects[i] = new WithdrawlThread(threadName, account);
            final WithdrawlThread wr = withdrawObjects[i];
            //create a new withdrawl thread or wt from the wr runnable object
            Thread wt = new Thread(wr);
            //store the created thread in the thread array
            myThreads[threadCounter] = wt;
            threadCounter++;
            wt.start();
         }
        // Sleep the main thread for the amount of time set from the command line
        try 
        {
            Thread.sleep(waitTime);            
        } 
        catch (InterruptedException e) {
            System.out.println("Main Thread Interupted");
        }

        for(threadCounter=0; threadCounter < totalThreads; threadCounter++)
        {
                //System.out.println("Thread name: " +myThreads[threadCounter].getName());
                myThreads[threadCounter].interrupt();
        }
        System.out.println("    Deposit Thread Summary");
        System.out.println("--------------------------------");
        for(int i = 0; i<numDThreads; i++){
            System.out.println("-Deposit Thread name    : "+depositObjects[i].getName());
            System.out.println("-Number of Deposits     : "+depositObjects[i].getNumDeposits());
            System.out.println("-Total Amount Deposited : "+depositObjects[i].getTotalDeposited());
            System.out.println("-Total Amount of Waits  : "+depositObjects[i].getNumWaits()+"\n");
        }
        System.out.println("--------------------------------");
        System.out.println("    Withdrawl Thread Summary");
        System.out.println("--------------------------------");
        for(int i = 0; i<numWThreads; i++){
            System.out.println("-Withdrawl Thread name: " + withdrawObjects[i].getName());
            System.out.println("-Number of Withdrawls: " + withdrawObjects[i].getNumWithdrawls());
            System.out.println("-Total Amount Withdrawn: " + withdrawObjects[i].getTotalWithdrawn());
            System.out.println("-Total Amount of Waits: " + withdrawObjects[i].getNumWaits()+"\n");
        }
        System.out.println("--------------------------------");
        System.out.println("Final Balance: " + account.getBalance());
        System.exit(0);
    }
}
