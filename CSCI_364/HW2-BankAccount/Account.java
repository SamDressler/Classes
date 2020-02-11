//Sam Dressler 
//
public class Account
{
    private static Account account = new Account();
    private int balance = 0;
    
    public Account()
    {

    }
   
     public static Account getInstance()
    {
        return account;        
    }
    
    public void setBalance(int balance)
    {
        this.balance = balance;
    }
    public int getBalance()
    {
        return balance;    
    }
    
}
