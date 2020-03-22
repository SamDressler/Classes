/**
 * 
 */
package myrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author david
 *
 */
public interface Hello extends Remote {
	String sayHello(String name) throws RemoteException;
}
