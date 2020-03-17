//Sam Dressler
package server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import api.Worker;
import api.ClientManager;
import java.rmi.Remote;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class RemoteObject extends UnicastRemoteObject implements ClientManager
{
    /**
     *  Requires this
     */
    private static final long serialVersionUID = 1L;
    private final String[] tasks = { "SortWorker", "SumReducer", "PrimeChecker", "FractionReducer" };
    private final HashMap<String, Integer> usersMap = new HashMap<String, Integer>();
    
    /**
     * Registers a client with the volunteer computing server.
     * 
     * @param userid the client name
     * @return a List of available work task names. The names are unique.
     * @throws RemoteException for no reason
     */
    public List<String> register(final String userID) throws RemoteException 
    {
        //final UserData userData = new UserData(userID);
        //register the userID with an initial score of 0
        usersMap.put(userID, 0);
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
    public Worker requestWork(final String userId, final String taskName) throws RemoteException {
        
      
    }

    /**
     * Submits work results from a volunteer to the server.
     * 
     * @param userId the client submitting results
     * @param answer the completed Worker
     * @throws RemoteException if userId is unknown or if an invalid worker is
     *                         submitted to the remote object
     */
    public void submitResults(final String userId, final Worker answer) throws RemoteException {

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
        return 1.0f;
    }

}
