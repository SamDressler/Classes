package myrmi;
import java.rmi.Naming;
public class Client {
    public static void main(String[] args) {
        if(args.length!= 1){
            System.out.println("Must have 1 arg");   
        }
            String host = args[0];
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try 
        {
            String url = "rmi://" + host + "/Hello";
            Hello stub = (Hello)Naming.lookup(url);
            System.out.println("response: " + stub.sayHello(null));
            System.out.println("response: " + stub.sayHello("UND"));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
}
}
