//Sam Dressler 
//
public class Account
{
    private static Account account = new Account();
    private Double balance = 0.;
    
    public Account()
    {
        
    }
   
     public static Account getInstance()
    {
        return account;        
    }
    
    public void setBalance(double balance)
    {
        this.balance = balance;
    }
    public Double getBalance()
    {
        return balance;    
    }
    public synchronized void deposit(Double amount){
        if(amount > 0.0 ){
            balance += amount;
        }
        else{
            System.out.println("Amount to deposit must be greater than 0");
        }
    }
    
}
