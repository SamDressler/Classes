//Sam Dressler
//HW 3 Client
package client;

public class Client
{
    private static String user_id;
    private static int rmi_port = 1099;
    private static String rmi_host = "localhost";

    public static void main(final String args[]) 
    {
        final int num_iterations = 5;
        read_args(args);
        try {
            final String url = "rmi://" + rmi_host + "ClientManager";
            //Register once

            //Request and do work 5 times
            for (int i = 0; i < num_iterations; i++) 
            {
                
                //Print score
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * read_args takes the users input and fills the useful variables
     */
    private static void read_args(final String args[]) 
    {
        final int num_args = args.length;
        switch(num_args)
        {
            //just client userid is specified
            case 1:
                user_id = args[0];
            break;

            //remi host
            case 2:
                user_id = args[0];
                rmi_host = args[1];
            break;
            //rmi port
            
            case 3:
                user_id = args[0];
                rmi_host = args[1];
                rmi_port = Integer.parseInt(args[2]);
            break;
            
            //all default values are used
            default:

            break;
        }
    }
}
