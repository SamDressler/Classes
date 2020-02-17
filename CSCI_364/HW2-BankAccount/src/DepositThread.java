//Sam Dressler 
//HW2 CSCI 364
//2/17/2020
//Deposit Runnable 
import java.util.concurrent.ThreadLocalRandom;

// The Producer
public class DepositThread implements Runnable 
{
    private Double maxBalance;
    private Account account;
    private double totalDeposited = 0;
    private int numDeposits = 0;
    private int numWaits = 0;
    private String name;

    public DepositThread(final String name, final double max, final Account userAccount) 
    {
        this.maxBalance = max;
        this.account = userAccount;
        this.name = name;
    }

    @Override
    public void run()
    {
        while (true) 
        {
            final int amount = ThreadLocalRandom.current().nextInt(1, 100);
            try 
            {
                //System.out.println("Depsiting: " + amount);
                deposit(amount);
            } 
            catch (final InterruptedException e) 
            {
                break;
            }
        }
    }

    // depsoit method, contains the critical section for a depositing thread
    private void deposit(final int amount) throws InterruptedException
     {
        synchronized (account) 
        {
            while ((account.getBalance() + amount) > maxBalance) 
            {
                // System.out.println(name+": Max Balance Reached! The balance must decrease");
                try 
                {
                    account.wait();
                    numWaits++;
                } 
                catch (final InterruptedException e) 
                {
                    break;
                }
            }
            account.setBalance((account.getBalance() + amount));
            totalDeposited += amount;
            numDeposits++;
            account.notifyAll();
        }
    }
    public String getName()
    {
        return this.name;
    }

    public int getNumDeposits()
    {
        return this.numDeposits;
    }

    public double getTotalDeposited()
    {
        return this.totalDeposited;
    }

    public int getNumWaits()
    {
        return this.numWaits;
    }
}
