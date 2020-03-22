/**
 * 
 */
package myrmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author david
 *
 */
public class Server /**/extends UnicastRemoteObject implements Hello {

	public Server() /**/throws RemoteException {
		super();
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		StringBuilder sb = new StringBuilder("Hello, ");
		if (name == null) {
			sb.append("world");
		} else {
			sb.append(name);
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.out.println("new security manager");
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			Hello obj = new Server();
			/*
			Hello stub = (Hello)UnicastRemoteObject.exportObject(obj, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Hello", stub);
  			*/

			Naming.rebind("Hello", obj);
			
			System.out.println("Server ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
