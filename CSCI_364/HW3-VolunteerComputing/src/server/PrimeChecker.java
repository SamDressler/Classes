/**
 * PrimeChecker.java
 */
package server;
import api.Worker;
/**
 * @author Sam Dressler
 */
public class PrimeChecker extends Worker{

    private static final long serialVersionUID = -5335671218528869555L;
    private int number = 0;
    private boolean isPrime = false;
    public PrimeChecker(int task_id, int number){
        super(task_id,"PrimeChecker");
        this.number = number;
    }
    @Override
    public void doWork(){
        checkPrime(number);
    }
    /**
     * checkPrime
     * @Description This function does the processing required to check if a number
     * is prime or not
     * @param Integer to be checked if prime or not
     * @return none
     */
    private void checkPrime(int num){
        isPrime = false;
        if(num == 0 || num == 1){
            //not prime
        }
        if((num % 2) == 0){
            //Not prime
        }
        else if(num % 3 == 0){
            //Not prime
        }
        else if(num % 4 == 0){
            //Not Prime
        }
        else if(num % 5 == 0){
            //Not Prime
        }
        else if(num % 6 == 0){
            //Not Prime
        }
        else if(num % 7 == 0){
            //Not Prime
        }
        else if(num % 8 == 0){
            //Not Prime
        }
        else if(num % 9 == 0){
            //Not Prime
        }
        else{
            isPrime = true;
        }if(isPrime){
            System.out.println(number+" is Prime!");
        }else{
            System.out.println(number+" is not Prime!");
        }
    }
    public boolean isNumPrime(){
        return this.isPrime;
    }
    public int getNum(){
        return this.number;
    }
    /**
     * Unit Testing method
     */
	public static void main(String[] args) {
        Worker worker = new PrimeChecker(0, 67); //Prime
        worker.doWork();
	}

}