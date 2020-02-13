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
        // get singleton from the private constructor;
        final Account account = Account.getInstance();
        account.setBalance(balance);
        System.out.println("Starting Balance: " + account.getBalance());
        System.out.println("---------------------------");
        String threadName;
        // initialize the deposit threads
       // for (int i = 0; i < numDThreads; i++) 
       // {
            int i = 0;
            threadName = "Depsoitor_" + i;
            final DepositThread dt = new DepositThread(threadName, maxBalance, account);
            Thread d1t = new Thread(dt);
            System.out.println("Depositor " + i + " created");
      //  }

        // Initialize the withdrawl threads
       // for (int i = 0; i < numWThreads; i++) 
      //  {
            i = 0;
            threadName = "Withdrawer_" + i;
            final WithdrawlThread wt = new WithdrawlThread(threadName, account);
            Thread w1t = new Thread(wt);
            System.out.println("Withdrawer " + i + " created");
       // }
        // Sleep the main thread for the amount of time set from the command line
        final Thread t = Thread.currentThread();

        t.setName("Main Thread");
        System.out.println(t.getName() + " is sleeping"); 
        try 
        {
            d1t.start();
            w1t.start();
            Thread.sleep(waitTime);
        } 
        catch (InterruptedException e) {
            System.out.println("Main Thread Interupted");
            e.printStackTrace();
        }
        System.out.println("Final Balance: " + account.getBalance());
    }
}
