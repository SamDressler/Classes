/**
 * 
 */
package myrmi;

import java.rmi.Naming;

/**
 * @author david
 *
 */
public class Client {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: $ java Client <server host>");
			System.exit(1);
		}
		
		String host = args[0];
		
		if (System.getSecurityManager() == null) {
			System.out.println("new security manager");
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
//			Registry registry = LocateRegistry.getRegistry(host);
//			Hello stub = (Hello)registry.lookup("Hello");
			String url = "rmi://" + host + "/Hello";
			Hello stub = (Hello)Naming.lookup(url);
//			String[] names = Naming.list(url);
//			System.out.println("remote methods: ");
//			for (String name : names) {
//				System.out.println("\t" + name);
//			}
			String response = stub.sayHello("UND");
			System.out.println("response: " + response);
			
			response = stub.sayHello("David");
			System.out.println("response: " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
