//Sam Dressler
//Deposit runnable class 
import java.util.concurrent.ThreadLocalRandom;

// The Producer
public class DepositThread implements Runnable 
{
    private Double maxBalance;
    private Account account;

    public DepositThread(final String threadName, final double max, final Account userAccount) 
    {
        this.maxBalance = max;
        this.account = userAccount;
        Thread.currentThread().setName(threadName);
    }

    @Override
    public void run()
    {
        while (true) 
        {
            final int amount = ThreadLocalRandom.current().nextInt(1, 100);
            try 
            {
                System.out.println("Depsiting: " + amount);
                deposit(amount);
            } 
            catch (final InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
    }

    // depsoit method, contains the critical section for a depositing thread
    private void deposit(final int amount) throws InterruptedException {
        synchronized (account) {
            final String name = Thread.currentThread().getName();
            System.out.println("Current Working thread: " + name);
            while ((account.getBalance() + amount) > maxBalance) {
                // System.out.println(name+": Max Balance Reached! The balance must decrease");
                try {
                    this.wait();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(100);
            account.setBalance((account.getBalance() + amount));
            System.out.println(name + " deposited " + amount);
            System.out.println("New Account Balance: " + account.getBalance());
            System.out.println("---------------------------");
            account.notifyAll();
        }
    }
}
