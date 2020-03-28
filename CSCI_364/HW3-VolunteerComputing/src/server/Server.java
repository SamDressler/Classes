//Sam Dressler
//Server.java
package server;
import api.ClientManager;
import java.rmi.Naming;

public class Server
{
    private static String rmi_host = "localhost";
    private static int rmi_port    = 1099;
    public static void main(String [] args)
    {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        //read in the args
        read_args(args);
        try{
            String url = "rmi://" + rmi_host + ":" + rmi_port + "/" + ClientManager.REMOTE_OBJECT;
            System.out.println(url);
            Naming.bind(url, new RemoteObject());
            System.out.println("Server is ready!");
            System.out.println("-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

//Read_args - Reads the user args
    private static void read_args(final String args[]) 
{
    final int num_args = args.length;
    switch(num_args)
    {
        //just the RMI host is specified
        case 1:
           rmi_host = args[0];
        break;

        //remi host
        case 2:
            rmi_host= args[0];
            rmi_port = Integer.parseInt(args[1]);
        break;
        
        //all default values are used
        default:
            break;
    }
}
}
