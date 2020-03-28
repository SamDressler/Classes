//Sam Dressler
package server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import api.Worker;
import api.ClientManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObject extends UnicastRemoteObject implements ClientManager
{
    /**
     *  Requires this
     */
    private static final long serialVersionUID = 1L;
    
    private final String[] tasks = { "SortWorker", "SumReducer", "PrimeChecker", "FractionReducer" };
    private final HashMap<String, Integer> usersMap = new HashMap<String, Integer>();
    private int task_id = 0;
    private String sumWorkerType;
    private int num_sr_params=0;
    public RemoteObject() throws RemoteException{

    }

    public HashMap<String,Integer> getUsers()
    {
        return usersMap;
    }
    public int genTaskId(){
        return this.task_id++;
    }
    /**
     * Registers a client with the volunteer computing server.
     * 
     * @param userid the client name
     * @return a List of available work task names. The names are unique.
     * @throws RemoteException for no reason
     */
    public List<String> register(final String userID) throws RemoteException 
    {
        //register the userID with an initial score of 0 if the map doesn't already 
        //contain the user.
        if(!usersMap.containsKey(userID)){
            usersMap.put(userID, 0);
            System.out.println("Registered new user: "+userID);
        }
        else
        {
            System.out.println("-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-");
            System.out.println(userID+" has returned!");
        }

        System.out.println("All Users: "+usersMap.keySet().toString());
        System.out.println("All Scores: "+usersMap.values().toString());
        System.out.println("-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-");
        //returns the list of tasks to the client
        return Arrays.asList(tasks);
        
    }

    /**
     * Requests work from the server.
     * 
     * @param userId   the client requesting work
     * @param taskName the project name
     * @return a Worker object that can perform
     * @throws RemoteException if userId is unknown or if taskName does not exist.
     */
    public Worker requestWork(final String userId, final String taskName) throws RemoteException 
    {    
        Worker worker = null;
        Random rand = new Random();
        switch(taskName)
        {
            case "SortWorker":
               int num_sw_params = (rand.nextInt(5)+1);
               Integer[] sw_params = new Integer[num_sw_params];
               for(int i = 0; i <num_sw_params; i ++){
                   sw_params[i] = rand.nextInt(1000);
               }
               worker = new SortWorker<Integer>(genTaskId(),sw_params);
                break;
            case "SumReducer":
               num_sr_params = (rand.nextInt(5)+1);
               int type = rand.nextInt(3);

                if(type == 0) //Random Choice is Integer
                {
                    Integer[] sr_params = new Integer[num_sr_params];
                    for(int i = 0; i <num_sr_params; i++){
                        sr_params[i] = rand.nextInt(1000);
                    }
                    sumWorkerType="Integer";
                    worker = new SumReducer<Integer>(genTaskId(),sr_params);
                }
                else if(type == 1)//Random Choice is Float
                {
                    Float[] sr_params = new Float[num_sr_params];
                    for(int i = 0; i <num_sr_params; i++){
                        sr_params[i] = rand.nextFloat()*(1000 - 1)+1;
                    }
                    sumWorkerType="Float";
                    worker = new SumReducer<Float>(genTaskId(),sr_params);
                }
                else if(type == 2)//Random Choice is Double
                {
                    Double[] sr_params = new Double[num_sr_params];
                    for(int i = 0; i <num_sr_params; i++){
                        sr_params[i] = rand.nextDouble() * (1000 - 1)+1;
                    }
                    sumWorkerType="Double";
                    worker = new SumReducer<Double>(genTaskId(),sr_params);
                }
                
                break;

            case "PrimeChecker":
               int randomNum = rand.nextInt(1000);
               worker = new PrimeChecker(genTaskId(),randomNum);
                break;

            case "FractionReducer":
                int numerator = rand.nextInt(1000);
                int denominator = rand.nextInt(1000);
                worker = new FractionReducer(genTaskId(),numerator,denominator);
                break;

            default:
                String s = "Invalid Worker";
                throw new RemoteException(s);           
        }
        return worker;
    }
    /**
     * Submits work results from a volunteer to the server.
     * 
     * @param userId the client submitting results
     * @param answer the completed Worker
     * @throws RemoteException if userId is unknown or if an invalid worker is
     *                         submitted to the remote object
     */
    public void submitResults(final String userId, final Worker answer) throws RemoteException 
    {
       
        if(!usersMap.containsKey(userId))
        {
            String s = "ERROR: User not recognized";
            throw new RemoteException(s);
        }
        else
        {
            String taskname = answer.getTaskName();
            switch(taskname){
                case "SortWorker":
                    @SuppressWarnings("unchecked")
                    SortWorker<? extends Number> sortWorker = (SortWorker<? extends Number>) answer;
                    System.out.println("|-Input-Unsorted: "+ sortWorker.getInputList());
                    System.out.println("|-Output-Sorted: "+sortWorker.getList());
                    System.out.println("");
                    break;
                case "SumReducer":
                    @SuppressWarnings("unchecked")
                    SumReducer<Double> sumWorker = (SumReducer<Double>) answer;
                    System.out.println("|-Total from "+ num_sr_params+" "+sumWorkerType+"s is: "+sumWorker.getTotal());
                    System.out.println("");
                    break;

                case "PrimeChecker":

                    PrimeChecker primeWorker = (PrimeChecker) answer;
                    if(primeWorker.isNumPrime()){
                        System.out.println("|-Integer with value: "+primeWorker.getNum()+" is prime!");
                        System.out.println("");
                    }
                    else{
                        System.out.println("|-Integer with value: "+primeWorker.getNum()+" is not prime!");
                        System.out.println("");
                    }
                    break;
 
                case "FractionReducer":
                    FractionReducer fractionWorker = (FractionReducer) answer;
                    System.out.println("|-Input Fraction: "+fractionWorker.getNumerator()+"/"+fractionWorker.getDenominator());
                    System.out.println("|-Greatest Common Denominator: "+fractionWorker.getGCD());
                    System.out.println("|-Output Fraction: "+fractionWorker.getNewNumerator()+"/"+fractionWorker.getNewDenominator());
                    System.out.println("");
                    break;
 
             default:
                 String s = "Invalid Worker";
                 throw new RemoteException(s);  
            }
            //Incremenet the score for the user
            usersMap.put(userId,(usersMap.get(userId)+1));
        }
        
    }

    /**
     * Gets the score for a specified client.
     * 
     * @param userid the client name
     * @return the client's current score
     * @throws RemoteException if userId is unknown
     */
    public float getScore(final String userId) throws RemoteException
    {
            return usersMap.get(userId);
    }

}
