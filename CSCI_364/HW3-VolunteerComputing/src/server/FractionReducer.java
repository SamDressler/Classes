//Fraction Reducer.ava
package server;
import api.Worker;
public class FractionReducer extends Worker
{
    private static final long serialVersionUID = 1768991591643396497L;
    private int numerator;
    private int denominator;
    private int GCD;
    private int new_numerator;
    private int new_denominator;

    public FractionReducer(int task_id, int numerator, int denominator)
    {
        super(task_id, "FractionReducer");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Find GCD 
     * 
     */
    private int findGCD(int numerator, int denominator){
        int gcd=0;
        for(int i = 1; i <= numerator && i <= denominator; i++)
        {
            // Checks if i is factor of both integers
            if(numerator % i==0 && denominator % i==0)
                gcd = i;
        }
        return gcd;
    }
    @Override
    public void doWork(){
        this.GCD = findGCD(numerator, denominator);
        this.new_numerator   = (numerator/GCD);
        this.new_denominator = (denominator/GCD);
    }
    public int getNumerator(){
        return this.numerator;
    }
    public int getNewNumerator(){
        return this.new_numerator;
    }
    public int getDenominator(){
        return this.denominator;
    }
    public int getNewDenominator(){
        return this.new_denominator;
    }
    public int getGCD(){
        return this.GCD;
    }
    /**
     * Test Program
     * Note: Need to add print statements to the doWork to see the results;
     */
    public static void main(String[] args){
        FractionReducer fd = new FractionReducer(0, 81, 153);
        fd.doWork();

    }
}