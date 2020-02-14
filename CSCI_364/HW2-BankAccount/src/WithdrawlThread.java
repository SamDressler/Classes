import java.util.concurrent.ThreadLocalRandom;

// The consumer
public class WithdrawlThread implements Runnable 
{
    private Account account;
    private double totalWithdrawn = 0;
    private int numWithdrawls = 0;
    private int numWaits = 0;
    private String name;
    public WithdrawlThread(final String name, final Account userAccount)
    {
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
                //System.out.println("Withdrawing: " + amount);
                withdraw(amount);

            } 
            catch (final InterruptedException e) 
            {
                break;
            }
        }
    }

    private void withdraw(final int amount) throws InterruptedException 
    {
        synchronized (account) 
        {
            //final String name = Thread.currentThread().getName();
            //System.out.println("Current Working thread: " + name);

            while ((account.getBalance() - amount) <= 0) 
            {
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

            Thread.sleep(100);
            account.setBalance((account.getBalance()-amount));
            totalWithdrawn += amount;
            numWithdrawls++;
            account.notifyAll();
        }
    }
    public String getName()
    {
        return this.name;
    }

    public int getNumWithdrawls()
    {
        return this.numWithdrawls;
    }

    public double getTotalWithdrawn()
    {
        return this.totalWithdrawn;
    }

    public int getNumWaits()
    {
        return this.numWaits;
    }
}
